package com.msa4lms.domain.enrollment.controllers;

import com.msa4lms.domain.enrollment.responses.EnrollmentListRes;
import com.msa4lms.domain.enrollment.services.EnrollmentService;
import com.msa4lms.global.responses.GlobalRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    /**
     * 내 수강 내역 목록과 총 신청 학점 조회
     */
    @GetMapping("/enrollments/my")
    public ResponseEntity<GlobalRes<EnrollmentListRes>> getMyEnrollments(
            @RequestParam(name = "studentId") Long studentId,
            @RequestParam(name = "year", defaultValue = "2024") int year,
            @RequestParam(name = "semester", defaultValue = "1") int semester) {
        
        EnrollmentListRes result = enrollmentService.getMyEnrollments(studentId, year, semester);
        
        return ResponseEntity.status(200).body(
            GlobalRes.<EnrollmentListRes>builder()
                .code("00")
                .message("수강 내역 조회가 완료되었습니다.")
                .data(result)
                .build()
        );
    }
}
