package com.msa4lms.domain.professor.controllers;

import com.msa4lms.domain.professor.responses.ProfessorProfileRes;
import com.msa4lms.domain.professor.services.ProfessorService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/professor")
@RequiredArgsConstructor
public class ProfessorController {
    private final ProfessorService professorService;

    @GetMapping("/profile")
    public ResponseEntity<GlobalRes<ProfessorProfileRes>> getProfile(
            @AuthenticationPrincipal Claims claims
            ){
        ProfessorProfileRes profile = professorService.getProfile(Long.parseLong(claims.getSubject()));

        return ResponseEntity.ok(
                GlobalRes.<ProfessorProfileRes>builder()
                        .code("00")
                        .message("프로필 조회 성공")
                        .data(profile)
                        .build()
        );
    }
}
