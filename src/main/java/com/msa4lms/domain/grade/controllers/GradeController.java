package com.msa4lms.domain.grade.controllers;

import com.msa4lms.domain.grade.services.GradeService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GradeController {

    private final GradeService gradeService;

    /**
     * 내 성적 및 GPA 조회
     */
    @GetMapping("/student/grades")
    public ResponseEntity<GlobalRes<com.msa4lms.domain.grade.responses.GradeSummaryRes>> getGrades(
            @AuthenticationPrincipal Claims claims,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer semester) {
        Long userId = Long.parseLong(claims.getSubject());
        com.msa4lms.domain.grade.responses.GradeSummaryRes data = gradeService.getGradeSummary(userId, year, semester);

        return ResponseEntity.ok(
            GlobalRes.<com.msa4lms.domain.grade.responses.GradeSummaryRes>builder()
                .code("00")
                .message("성적 조회가 완료되었습니다.")
                .data(data)
                .build()
        );
    }

}
