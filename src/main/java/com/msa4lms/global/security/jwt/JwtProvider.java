package com.msa4lms.global.security.jwt;

import com.msa4lms.domain.user.entities.User;
import com.msa4lms.global.errors.custom.InvalidTokenException;
import com.msa4lms.global.security.cookie.CookieManager;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtProvider {
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final CookieManager cookieManager;

    public JwtProvider(JwtConfig jwtConfig, CookieManager cookieManager) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.secret()));
        this.cookieManager = cookieManager;
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

    // cookie에서 refreshToken추출
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return cookieManager.getCookies(request, jwtConfig.refreshTokenCookieName())
                .map(Cookie::getValue);
    }

    // 토큰 검증 및 클레임 추출
    public Claims extractClaims(String token) {
        try{
            // 토큰 유효성 체크와 토큰 분해
            return Jwts.parser()
                    .verifyWith(this.secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("토큰이 만료되었습니다.");
        }catch (UnsupportedJwtException e) {
            // 시그니처가 잘못됨
            throw new InvalidTokenException("서명이 위조된 토큰입니다.");
        } catch (MalformedJwtException e) {
            // jwt 포맷이 이상하다 (토큰 형식이 올바르지 않을 때)
            throw new InvalidTokenException("토큰 형식이 올바르지 않습니다.");
        } catch (JwtException | IllegalArgumentException e) {
            // 위 에러 이외 에러 받겠다. | 내부 처리 할 때 이상 발생시 처리
            // 나머지 토큰 인정 관련 에러 다 받겠다.
            throw new InvalidTokenException("토큰 검증에 실패했습니다.");
        }
    }

    // accessToken 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtConfig.headerKey());

        if(bearerToken == null || !bearerToken.startsWith(jwtConfig.scheme())){
            return Optional.empty();
        }

        return Optional.of(bearerToken.substring(jwtConfig.scheme().length()).trim());
    }

}


