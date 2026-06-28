package com.msa4lms.domain.grade.controllers;

import com.msa4lms.domain.grade.services.StudentGradeService;
import com.msa4lms.global.annotations.LoginUserId;
import com.msa4lms.global.responses.GlobalRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student/grades")
public class StudentGradeController {

    private final StudentGradeService gradeService;

    /**
     * 내 성적 및 GPA 조회
     */
    @GetMapping
    public ResponseEntity<GlobalRes<com.msa4lms.domain.grade.responses.GradeSummaryRes>> getGrades(
            @LoginUserId Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer semester) {
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
