package com.example.todolist.filter;

import com.example.todolist.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String authorization = httpServletRequest.getHeader("Authorization");
        String jwtToken = jwtTokenProvider.convertToken(authorization);
        String reqUri = httpServletRequest.getRequestURI();

        if(reqUri.startsWith("/auth") && !reqUri.startsWith("/authenticated")) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
