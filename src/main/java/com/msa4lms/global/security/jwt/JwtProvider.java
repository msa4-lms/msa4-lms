package com.msa4lms.global.security.jwt;

import com.msa4lms.domain.user.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.secret()));
    }

    private String generateToken(User user, long ttl){
        Date now = new Date();

        return Jwts.builder()
                .header()
                .type(jwtConfig.type())
                .and()
                .subject(String.valueOf(user.getId()))
                .issuer(jwtConfig.issuer())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + ttl))
                .claim("role",user.getRole().name())
                .signWith(secretKey)
                .compact();
    }

    // 외부에서 accesstoken 호출
    public String generateAccessToken(User user) {
        return this.generateToken(user, jwtConfig.accessTokenExpiry());
    }

    // 외부에서 refreshtoken 호출
    public String generateRefreshToken(User user) {
        return this.generateToken(user, jwtConfig.refreshTokenExpiry());
    }
}
