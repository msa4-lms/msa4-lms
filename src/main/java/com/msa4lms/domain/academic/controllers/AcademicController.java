package com.msa4lms.domain.academic.controllers;

import com.msa4lms.domain.academic.responses.AcademicAttendanceRes;
import com.msa4lms.domain.academic.responses.GradeSummaryRes;
import com.msa4lms.domain.academic.services.AcademicService;
import com.msa4lms.global.responses.GlobalRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/academic")
public class AcademicController {
    private final AcademicService academicService;

    /**
     * 내 성적 및 GPA 조회
     * TODO: 인증 연동 후 세션에서 studentId 추출 (현재는 쿼리 파라미터로 임시 구현)
     */
    @GetMapping("/grades")
    public ResponseEntity<GlobalRes<GradeSummaryRes>> getGrades(@RequestParam long studentId) {
        GradeSummaryRes data = academicService.getGradeSummary(studentId);
        
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
    public ResponseEntity<GlobalRes<List<AcademicAttendanceRes>>> getAttendance(@RequestParam long studentId) {
        List<AcademicAttendanceRes> data = academicService.getAttendance(studentId);

        return ResponseEntity.ok(
            GlobalRes.<List<AcademicAttendanceRes>>builder()
                .code("00")
                .message("출결 조회가 완료되었습니다.")
                .data(data)
                .build()
        );
    }
}
