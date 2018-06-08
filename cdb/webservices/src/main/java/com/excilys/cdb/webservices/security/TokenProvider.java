package com.excilys.cdb.webservices.security;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Component
public class TokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    @Value("${spring.security.authentication.jwt.validity}")
    private String tokenValidityInMilliSeconds;

    @Value("${spring.security.authentication.jwt.secret}")
    private String secretKey;

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.joining(","));

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expirationDateTime = now.plus(Long.valueOf(this.tokenValidityInMilliSeconds), ChronoUnit.MILLIS);

        Date issueDate = Date.from(now.toInstant());
        Date expirationDate = Date.from(expirationDateTime.toInstant());

        return Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, authorities)
                    .signWith(SignatureAlgorithm.HS512, this.secretKey).setIssuedAt(issueDate).setExpiration(expirationDate).compact();
    }

    public Authentication getAuthentication(String token) {

        Claims claims = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays.asList(claims.get(AUTHORITIES_KEY).toString().split(",")).stream()
                    .map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String authToken) {

        try {
            Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.info("Invalid JWT signature: " + e.getMessage());
            LOGGER.debug("Exception " + e.getMessage(), e);
            return false;
        }
    }
}