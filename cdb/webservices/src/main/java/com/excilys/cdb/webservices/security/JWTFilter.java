package com.excilys.cdb.webservices.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.excilys.cdb.webservices.config.WebServiceSecurityConfig;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JWTFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTFilter.class);

    private final TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {

        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
        ServletException {

        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String jwt = this.resolveToken(httpServletRequest);
            if (StringUtils.hasText(jwt)) {
                if (this.tokenProvider.validateToken(jwt)) {
                    Authentication authentication = this.tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);

            this.resetAuthenticationAfterRequest();
        } catch (ExpiredJwtException eje) {
            LOGGER.info("Security exception for user {} - {}", eje.getClaims().getSubject(), eje.getMessage());
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            LOGGER.debug("Exception " + eje.getMessage(), eje);
        }
    }

    private void resetAuthenticationAfterRequest() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(WebServiceSecurityConfig.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String jwt = bearerToken.substring(7, bearerToken.length());
            return jwt;
        }
        return null;
    }
}