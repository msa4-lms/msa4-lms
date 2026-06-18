package com.msa4lms.domain.academic.controllers;

import com.msa4lms.domain.academic.responses.AcademicAttendanceRes;
import com.msa4lms.domain.academic.responses.GradeSummaryRes;
import com.msa4lms.domain.academic.services.AcademicService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student/academic")
public class AcademicController {
    private final AcademicService academicService;

    /**
     * 내 성적 및 GPA 조회
     */
    @GetMapping("/grades")
    public ResponseEntity<GlobalRes<GradeSummaryRes>> getGrades(@AuthenticationPrincipal Claims claims) {
        Long userId = Long.parseLong(claims.getSubject());
        GradeSummaryRes data = academicService.getGradeSummary(userId);
        
        return ResponseEntity.ok(
            GlobalRes.<GradeSummaryRes>builder()
                .code("00")
                .message("성적 조회가 완료되었습니다.")
                .data(data)
                .build()
        );
    }

    /**
     * 내 출결 현황 조회
     */
    @GetMapping("/attendance")
    public ResponseEntity<GlobalRes<List<AcademicAttendanceRes>>> getAttendance(@AuthenticationPrincipal Claims claims) {
        Long userId = Long.parseLong(claims.getSubject());
        List<AcademicAttendanceRes> data = academicService.getAttendance(userId);

        return ResponseEntity.ok(
            GlobalRes.<List<AcademicAttendanceRes>>builder()
                .code("00")
                .message("출결 조회가 완료되었습니다.")
                .data(data)
                .build()
        );
    }
}
