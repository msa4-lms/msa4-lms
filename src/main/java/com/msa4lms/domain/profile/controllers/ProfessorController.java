package com.msa4lms.domain.profile.controllers;

import com.msa4lms.domain.profile.responses.ProfessorProfileRes;
import com.msa4lms.domain.profile.services.ProfessorService;
import com.msa4lms.global.annotations.LoginUserId;
import com.msa4lms.global.responses.GlobalRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
            @LoginUserId Long userId
            ){
        ProfessorProfileRes profile = professorService.getProfile(userId);

        return ResponseEntity.ok(
                GlobalRes.<ProfessorProfileRes>builder()
                        .code("00")
                        .message("프로필 조회 성공")
                        .data(profile)
                        .build()
        );
    }
}
