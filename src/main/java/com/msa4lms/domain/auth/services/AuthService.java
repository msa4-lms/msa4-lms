package com.msa4lms.domain.auth.services;

import com.msa4lms.domain.auth.mapper.AuthMapper;
import com.msa4lms.domain.auth.requests.LoginReq;
import com.msa4lms.domain.auth.requests.PasswordChangeReq;
import com.msa4lms.domain.auth.responses.AuthRes;
import com.msa4lms.domain.user.entities.User;
import com.msa4lms.domain.user.mapper.UserMapper;
import com.msa4lms.domain.user.responses.UserRes;
import com.msa4lms.global.errors.custom.*;
import com.msa4lms.global.security.cookie.CookieManager;
import com.msa4lms.global.security.jwt.JwtConfig;
import com.msa4lms.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;
    private final AuthMapper authMapper;
    private final CookieManager cookieManager;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;

    public AuthRes login(HttpServletResponse response, LoginReq loginReq){
        // User 정보 획득
        User user = userMapper.findByLoginId(loginReq.loginId());

        // User 가입 여부 확인
        if(user == null) {
            throw new NotRegisteredException("아이디와 비밀번호를 확인해주세요.");
        }

        // 역할 제한
        if(!user.getRole().name().equals(loginReq.role())){
            throw new NotRegisteredException("로그인 유형이 일치하지 않습니다.");
        }

        // 비밀번호 체크
        if(!passwordEncoder.matches(loginReq.password(), user.getPassword())) {
            throw new NotRegisteredException("아이디와 비밀번호를 확인해주세요.");
        }

        return this.generateAuthentication(response, user);
    }

    // reissue
    public AuthRes reissue(HttpServletRequest request, HttpServletResponse response) {
        // refreshToken 획득
        Optional<String> refreshTokenOptional = jwtProvider.extractRefreshToken(request);
        if(refreshTokenOptional.isEmpty()) {
            throw new InvalidTokenException("토큰이 없습니다.");
        }

        String extractRefreshToken = refreshTokenOptional.get();

        int id = Integer.parseInt(jwtProvider.extractClaims(extractRefreshToken).getSubject());

        User user = userMapper.findByPk(id);

        // 유저 가입 여부 확인 및 비로그인 상태 확인
        if(user == null || user.getRefreshToken() == null) {
            throw new InvalidTokenException("유효하지 않은 회원의 토큰입니다.");
        }

        if(!user.getRefreshToken().equals(extractRefreshToken)) {
            throw new InvalidTokenException("토큰이 일치하지 않습니다.");
        }


        return this.generateAuthentication(response, user);
    }

    // logout
    public void logout(HttpServletResponse response, int id) {
        User user = userMapper.findByPk(id);

        if(user == null) {
            throw new InvalidTokenException("유효하지 않은 회원의 토큰입니다.");
        }

        authMapper.updateRefreshToken(id, null);

        cookieManager.setCookie(
                response
                , jwtConfig.refreshTokenCookieName()
                , null
                ,0
                ,jwtConfig.reissUri()
        );
    }

    // 엑세스토큰 및 리프레시토큰 생성 후, 리프레시 토큰 DB&Cookie 저장, AuthRes로 반환
    private AuthRes generateAuthentication(HttpServletResponse response, User user){
        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);

        authMapper.updateRefreshToken(user.getId(), newRefreshToken);

        cookieManager.setCookie(
                response
                ,jwtConfig.refreshTokenCookieName()
                ,newRefreshToken
                , jwtConfig.refreshTokenCookieExpiry()
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


    // 비밀번호 변경
    public void changePassword(int id, PasswordChangeReq req){

        User user = userMapper.findByPk(id);

        if(user == null) {
            throw new NotRegisteredException("사용자를 찾을 수 없습니다.");
        }

        // 같은 비밀번호 작성 제한
        if (passwordEncoder.matches(
                req.newPassword(),
                user.getPassword()
        )) {
            throw new PasswordSameException(
                    "현재 사용 중인 비밀번호와 동일합니다."
            );
        }

        // 암호화
        String encodedPassword =
                passwordEncoder.encode(req.newPassword());

        int result =userMapper.updatePassword(id, encodedPassword);

        if (result == 0) {
            throw new PasswordChangeFailedException("비밀번호 변경에 실패했습니다.");
        }
    }



}
