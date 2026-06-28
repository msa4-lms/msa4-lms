package com.msa4lms.domain.enrollment.controllers;

import com.msa4lms.domain.enrollment.requests.EnrollmentCreateReq;
import com.msa4lms.domain.enrollment.responses.EnrollmentRes;
import com.msa4lms.domain.enrollment.services.StudentEnrollmentService;
import com.msa4lms.global.annotations.LoginUserId;
import com.msa4lms.global.responses.GlobalRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentEnrollmentController {

    private final StudentEnrollmentService enrollmentService;

    /**
     * 내 수강 내역 목록과 총 신청 학점 조회
     */
    @GetMapping("/enrollments/my")
    public ResponseEntity<GlobalRes<EnrollmentRes>> getMyEnrollments(
            @LoginUserId Long userId,
            @RequestParam(name = "year", defaultValue = "2026") int year,
            @RequestParam(name = "semester", defaultValue = "1") int semester) {

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
            @LoginUserId Long userId,
            @RequestBody @Valid EnrollmentCreateReq req) {
        
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
            @LoginUserId Long userId,
            @RequestParam(name = "lectureId") Long lectureId) {
        
        enrollmentService.cancelEnrollment(userId, lectureId);
        return ResponseEntity.status(200).body(
            GlobalRes.<Void>builder()
                .code("00")
                .message("수강 취소가 완료되었습니다.")
                .build()
        );
    }
}
