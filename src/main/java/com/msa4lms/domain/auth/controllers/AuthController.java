package com.msa4lms.domain.auth.controllers;

import com.msa4lms.domain.auth.requests.LoginReq;
import com.msa4lms.domain.auth.requests.PasswordChangeReq;
import com.msa4lms.domain.auth.responses.AuthRes;
import com.msa4lms.domain.auth.services.AuthService;
import com.msa4lms.domain.user.entities.Role;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/student/login")
    public ResponseEntity<GlobalRes<AuthRes>> studentLogin(
            HttpServletResponse response,
            @RequestBody LoginReq loginReq
    ) {
        return ResponseEntity.ok(
                GlobalRes.<AuthRes>builder()
                        .code("00")
                        .message("학생 로그인 성공")
                        .data(authService.login(response, loginReq, Role.STUDENT))
                        .build()
        );
    }

    @PostMapping("/professor/login")
    public ResponseEntity<GlobalRes<AuthRes>> professorLogin(
            HttpServletResponse response,
            @RequestBody LoginReq loginReq
    ) {
        return ResponseEntity.ok(
                GlobalRes.<AuthRes>builder()
                        .code("00")
                        .message("교수 로그인 성공")
                        .data(authService.login(response, loginReq, Role.PROFESSOR))
                        .build()
        );
    }

    @PostMapping("/admin/login")
    public ResponseEntity<GlobalRes<AuthRes>> adminLogin(
            HttpServletResponse response,
            @RequestBody LoginReq loginReq
    ) {
        return ResponseEntity.ok(
                GlobalRes.<AuthRes>builder()
                        .code("00")
                        .message("관리자 로그인 성공")
                        .data(authService.login(response, loginReq, Role.ADMIN))
                        .build()
        );
    }

    @PostMapping("/reissue-token")
    public ResponseEntity<GlobalRes<AuthRes>> reissue(
            HttpServletRequest request
            ,HttpServletResponse response
    ) {
        return ResponseEntity.ok(
                GlobalRes.<AuthRes>builder()
                        .code("00")
                        .message("토큰 재발급 완료")
                        .data(authService.reissue(request, response))
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<GlobalRes<String>> logout(
            HttpServletResponse response
            , @AuthenticationPrincipal Claims claims
    ) {
        Long userId = Long.parseLong(claims.getSubject());
        authService.logout(response, userId);

        return ResponseEntity.ok(
                GlobalRes.<String>builder()
                        .code("00")
                        .message("로그아웃 완료")
                        .build()
        );
    }

    // 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity<GlobalRes<String>> changePassword(
            @AuthenticationPrincipal Claims claims,
            @Valid @RequestBody PasswordChangeReq req
    ) {
        Long userId = Long.parseLong(claims.getSubject());
        authService.changePassword(userId, req);

        return ResponseEntity.ok(
                GlobalRes.<String>builder()
                        .code("00")
                        .message("비밀번호 변경이 완료되었습니다.")
                        .build()
        );
    }
}
