package com.msa4lms.global.security.filter;

import com.msa4lms.global.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityAuthenticationProvider {
    private final JwtProvider jwtProvider;

    public Authentication authentication(String token) {
        Claims claims = jwtProvider.extractClaims(token);
        String role = claims.get("role", String.class);
        return new UsernamePasswordAuthenticationToken(
                claims
                , null
                , List.of(
                        new SimpleGrantedAuthority("ROLE_" + role)
        )
        );
    }
}
