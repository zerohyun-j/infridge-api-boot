package com.inthefridges.api.global.security;

import com.inthefridges.api.global.security.handler.CustomAuthenticationFailureHandler;
import com.inthefridges.api.global.security.handler.CustomAuthenticationSuccessHandler;
import com.inthefridges.api.global.security.jwt.JwtAuthenticationEntryPoint;
import com.inthefridges.api.global.security.jwt.filter.ExceptionHandlerFilter;
import com.inthefridges.api.global.security.jwt.filter.JwtFilter;
import com.inthefridges.api.global.security.oauth.repository.HttpCookieOAuthAuthorizationRequestRepository;
import com.inthefridges.api.global.security.oauth.service.CustomOAuthUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final HttpCookieOAuthAuthorizationRequestRepository authorizationRepository;
    private final CustomOAuthUserService customOauthUserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final JwtFilter jwtFilter;
    private final ExceptionHandlerFilter jwtExceptionFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)  // formLogin 페이지 사용 X
                .httpBasic(AbstractHttpConfigurer::disable)  // http 기본 검증 사용 X Bearer 사용 O
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorise -> authorise
                        .requestMatchers("/", "/index.html").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/img/**", "/image/**", "/images/**").permitAll()
                        .requestMatchers("/font/**", "/fonts/**").permitAll()
                        .requestMatchers("/file/**", "/files/**").permitAll()
                        .requestMatchers("/api/v*/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/v*/members/**").hasAnyAuthority("MEMBER","ADMIN")
                        .requestMatchers("/api/v*/fridges/**").hasAnyAuthority("MEMBER","ADMIN")
                        .requestMatchers("/api/v*/refresh/**").permitAll()
                        .anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorizationEndpointConfig ->
                                authorizationEndpointConfig.baseUri("/oauth2/authorization")
                                        // 사용자의 인증 요청을 임시로 보관하는 리포지토리에 대한 설정
                                        // 인증 과정을 모두 마친 후 리다이렉트할 프론트의 uri를 보관하는 과정이 있음
                                        .authorizationRequestRepository(authorizationRepository))
                        // 인증 코드를 전달 받기위한 uri
                        .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOauthUserService))
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler))
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtFilter, OAuth2AuthorizationRequestRedirectFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtFilter.class)
                .build();
    }

//    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

//        config.addAllowedOrigin("http://3.36.236.207"); // 프론트 IPv4 주소
//        config.addAllowedOrigin("http://13.209.220.63"); // 백엔드 IPv4 주소
//        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
