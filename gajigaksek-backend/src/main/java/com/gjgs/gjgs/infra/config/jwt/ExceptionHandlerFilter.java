package com.gjgs.gjgs.infra.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gjgs.gjgs.modules.exception.dto.ErrorResponse;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.exception.token.TokenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ExceptionHandlerFilter extends OncePerRequestFilter {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // objectMapper가 LocalDateTime을 인식할 수 있도록 하는 설정
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            setErrorResponse(response, e.getMessage(), e.getErrorCode(),e.getHttpStatus());
        } catch (MemberNotFoundException e) {
            setErrorResponse(response, e.getMessage(), e.getErrorCode(),e.getHttpStatus());
        }
    }

    private void setErrorResponse(HttpServletResponse response, String message, String errorCode, HttpStatus status) throws IOException {
        String errorResponse = objectMapper.writeValueAsString(
                ErrorResponse.of(message,errorCode));
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(errorResponse);
    }
}
