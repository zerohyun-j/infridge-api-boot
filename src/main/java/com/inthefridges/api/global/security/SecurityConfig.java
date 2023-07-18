package com.inthefridges.api.global.security;

import com.inthefridges.api.global.security.handler.CustomAuthenticationFailureHandler;
import com.inthefridges.api.global.security.handler.CustomAuthenticationSuccessHandler;
import com.inthefridges.api.global.security.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository;
import com.inthefridges.api.global.security.oauth.service.CustomOauthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final HttpCookieOAuthAuthorizationRequestRepository authorizationRepository;
    private final CustomOauthUserService customOauthUserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(CsrfConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)  // formLogin 페이지 사용 X
                .httpBasic(AbstractHttpConfigurer::disable)  // http 기본 검증 사용 X Bearer 사용 O
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorise -> authorise
                        .requestMatchers("/members/**").authenticated()
                        .anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorizationEndpointConfig ->
                                authorizationEndpointConfig.baseUri("/api/v1/oauth2/authorization")
                                        // 사용자의 인증 요청을 임시로 보관하는 리포지토리에 대한 설정
                                        // 인증 과정을 모두 마친 후 리다이렉트할 프론트의 uri를 보관하는 과정이 있음
                                        .authorizationRequestRepository(authorizationRepository))
                        // 인증 코드를 전달 받기위한 uri
                        .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig.baseUri("/api/v1/oauth2/callback/*"))
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOauthUserService))
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler))
//                        .exceptionHandling(exceptionHandling ->
//                                exceptionHandling
//                                        .authenticationEntryPoint(
//                                                (httpServletRequest, httpServletResponse, e) -> httpServletResponse.sendError(401)
//                                        )
//                                        .accessDeniedHandler(
//                                                (httpServletRequest, httpServletResponse, e) -> httpServletResponse.sendError(403)
//                                        ))
                .build();
    }
}
