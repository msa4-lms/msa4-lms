package com.msa4lms.domain.auth.controllers;

import com.msa4lms.domain.auth.requests.LoginReq;
import com.msa4lms.domain.auth.responses.AuthRes;
import com.msa4lms.domain.auth.services.AuthService;
import com.msa4lms.global.responses.GlobalRes;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<GlobalRes<AuthRes>> login(
            @Valid @RequestBody LoginReq loginReq
            , HttpServletResponse response
    ){
        authService.login(response, loginReq);

        return ResponseEntity.status(200).body(
                GlobalRes.<AuthRes>builder()
                        .code("00")
                        .message("로그인 성공")
                        .data(authService.login(response, loginReq))
                        .build()
        );
    }
}
