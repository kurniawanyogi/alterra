package com.clean.architecture.security;

import com.clean.architecture.common.ApiResponseStatus;
import com.clean.architecture.common.TokenUtil;
import com.clean.architecture.model.Authentication;
import com.clean.architecture.service.UserDetailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN = "TOKEN";

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private TokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(TOKEN);
        if (token != null) {
            String username = tokenUtil.getUsernameFromToken(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                com.clean.architecture.model.Authentication userDetails = (Authentication) userDetailService.loadUserByUsername(username);
                if (tokenUtil.isValidToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = tokenUtil.getAuthentication(token, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    log.error("auth failed");
                    unAuthorizeResponse(response);
                }
            }
        }
        filterChain.doFilter(request, response);
        response.getOutputStream().close();
    }

    private void unAuthorizeResponse(HttpServletResponse response) throws IOException {
        ApiResponseStatus baseResponse = new ApiResponseStatus();
        baseResponse.setCode(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
        baseResponse.setMessage("unAuthorization Access");

        byte[] responseToSend = restResponseByte(baseResponse);
        response.setHeader("Content-Type", "application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().write(responseToSend);
    }

    private byte[] restResponseByte(ApiResponseStatus errorResponse) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(errorResponse);
        return serialized.getBytes();
    }
}
