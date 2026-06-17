package com.msa4lms.domain.enrollment.controllers;

import com.msa4lms.domain.enrollment.requests.PostEnrollmentReq;
import com.msa4lms.domain.enrollment.responses.EnrollmentRes;
import com.msa4lms.domain.enrollment.services.EnrollmentService;
import com.msa4lms.global.responses.GlobalRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    /**
     * 내 수강 내역 목록과 총 신청 학점 조회
     */
    @GetMapping("/enrollments/my")
    public ResponseEntity<GlobalRes<EnrollmentRes>> getMyEnrollments(
            @RequestParam(name = "studentId") Long studentId,
            @RequestParam(name = "year", defaultValue = "2024") int year,
            @RequestParam(name = "semester", defaultValue = "1") int semester) {

        EnrollmentRes result = enrollmentService.getMyEnrollments(studentId, year, semester);

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
    public ResponseEntity<GlobalRes<Void>> applyEnrollment(@RequestBody @Valid PostEnrollmentReq req) {
        enrollmentService.applyEnrollment(req.studentId(), req.lectureId());
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
            @RequestParam(name = "studentId") Long studentId,
            @RequestParam(name = "lectureId") Long lectureId) {
        
        enrollmentService.cancelEnrollment(studentId, lectureId);
        return ResponseEntity.status(200).body(
            GlobalRes.<Void>builder()
                .code("00")
                .message("수강 취소가 완료되었습니다.")
                .build()
        );
    }
}