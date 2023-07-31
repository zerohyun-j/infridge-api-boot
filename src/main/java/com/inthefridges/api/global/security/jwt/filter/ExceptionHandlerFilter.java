package com.inthefridges.api.global.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.global.exception.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ServiceException e) {
            log.debug("[ExceptionHandler] token error message = {}", e.getMessage());
            generateErrorResponse(response, e);
        }
    }

    private void generateErrorResponse(HttpServletResponse response, ServiceException e) throws IOException {
        response.setStatus(e.getExceptionCode().getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.displayName());
        response.getWriter()
                .write(objectMapper.writeValueAsString(
                        ErrorResponse.of(e.getExceptionCode())));
    }
}
