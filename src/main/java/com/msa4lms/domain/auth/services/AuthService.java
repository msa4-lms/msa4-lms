package com.msa4lms.domain.auth.services;

import com.msa4lms.domain.auth.mapper.AuthMapper;
import com.msa4lms.domain.auth.requests.LoginReq;
import com.msa4lms.domain.auth.responses.AuthRes;
import com.msa4lms.domain.user.entities.User;
import com.msa4lms.domain.user.mapper.UserMapper;
import com.msa4lms.domain.user.responses.UserRes;
import com.msa4lms.global.errors.custom.NotRegisterdException;
import com.msa4lms.global.security.cookie.CookieManager;
import com.msa4lms.global.security.jwt.JwtConfig;
import com.msa4lms.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;
    private final AuthMapper authMapper;
    private final CookieManager cookieManager;
    private final JwtConfig jwtConfig;

    public AuthRes login(HttpServletResponse response, LoginReq loginReq){
        // User 정보 획득
        User user = userMapper.findByUserNo(loginReq.userNo());

        // User 가입 여부 확인
        if(user == null) {
            throw new NotRegisterdException("아이디와 비밀번호를 확인해주세요.");
        }

        // 역할 제한
        if(!user.getRole().name().equals(loginReq.role())){
            throw new NotRegisterdException("로그인 유형이 일치하지 않습니다.");
        }

        // 비밀번호 체크

        return this.generateAuthentication(response, user);
    }

    private AuthRes generateAuthentication(HttpServletResponse response, User user){
        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);

        authMapper.updateRefreshToken(user.getId(), newRefreshToken);

        cookieManager.setCookie(
                response
                ,jwtConfig.refreshTokenCookieName()
                ,newRefreshToken
                , jwtConfig.refreshTokenExpiry()
                , jwtConfig.reissUri()
        );

        return AuthRes.builder()
                .accessToken(newAccessToken)
                .user(
                        UserRes.builder()
                                .id(user.getId())
                                .loginId(user.getLoginId())
                                .name(user.getName())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .departmentId(user.getDepartmentId())
                                .createdAt(user.getCreatedAt())
                                .build()
                )
                .build();
    }

}
