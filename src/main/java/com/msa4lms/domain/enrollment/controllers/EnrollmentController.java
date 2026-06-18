package com.msa4lms.domain.enrollment.controllers;

import com.msa4lms.domain.enrollment.requests.PostEnrollmentReq;
import com.msa4lms.domain.enrollment.responses.EnrollmentRes;
import com.msa4lms.domain.enrollment.services.EnrollmentService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    /**
     * 내 수강 내역 목록과 총 신청 학점 조회
     */
    @GetMapping("/enrollments/my")
    public ResponseEntity<GlobalRes<EnrollmentRes>> getMyEnrollments(
            @AuthenticationPrincipal Claims claims,
            @RequestParam(name = "year", defaultValue = "2026") int year,
            @RequestParam(name = "semester", defaultValue = "1") int semester) {

        Long userId = Long.parseLong(claims.getSubject());
        EnrollmentRes result = enrollmentService.getMyEnrollments(userId, year, semester);

        return ResponseEntity.status(200).body(
            GlobalRes.<EnrollmentRes>builder()
                .code("00")
                .message("수강 내역 조회가 완료되었습니다.")
                .data(result)
                .build()
        );
    }

    /**
     * 수강 신청
     */
    @PostMapping("/enrollments")
    public ResponseEntity<GlobalRes<Void>> applyEnrollment(
            @AuthenticationPrincipal Claims claims,
            @RequestBody @Valid PostEnrollmentReq req) {
        
        Long userId = Long.parseLong(claims.getSubject());
        enrollmentService.applyEnrollment(userId, req.lectureId());
        return ResponseEntity.status(200).body(
            GlobalRes.<Void>builder()
                .code("00")
                .message("수강 신청이 완료되었습니다.")
                .build()
        );
    }

    /**
     * 수강 취소
     */
    @DeleteMapping("/enrollments")
    public ResponseEntity<GlobalRes<Void>> cancelEnrollment(
            @AuthenticationPrincipal Claims claims,
            @RequestParam(name = "lectureId") Long lectureId) {
        
        Long userId = Long.parseLong(claims.getSubject());
        enrollmentService.cancelEnrollment(userId, lectureId);
        return ResponseEntity.status(200).body(
            GlobalRes.<Void>builder()
                .code("00")
                .message("수강 취소가 완료되었습니다.")
                .build()
        );
    }
}
