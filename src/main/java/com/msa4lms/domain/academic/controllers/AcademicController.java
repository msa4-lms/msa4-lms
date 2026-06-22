package com.msa4lms.domain.academic.controllers;

import com.msa4lms.domain.academic.requests.ExcuseRequestReq;
import com.msa4lms.domain.academic.responses.AcademicAttendanceRes;
import com.msa4lms.domain.academic.responses.AttendanceRateRes;
import com.msa4lms.domain.academic.responses.ExcuseRequestRes;
import com.msa4lms.domain.academic.responses.GradeSummaryRes;
import com.msa4lms.domain.academic.services.AcademicService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<GlobalRes<GradeSummaryRes>> getGrades(
            @AuthenticationPrincipal Claims claims,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer semester) {
        Long userId = Long.parseLong(claims.getSubject());
        GradeSummaryRes data = academicService.getGradeSummary(userId, year, semester);
        
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

    @GetMapping("/attendance-rates")
    public ResponseEntity<GlobalRes<List<AttendanceRateRes>>> getAttendanceRates(
            @AuthenticationPrincipal Claims claims,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer semester) {
        Long userId = Long.parseLong(claims.getSubject());
        List<AttendanceRateRes> data = academicService.getAttendanceRates(userId, year, semester);

        return ResponseEntity.ok(
            GlobalRes.<List<AttendanceRateRes>>builder()
                .code("00")
                .message("출석률 조회가 완료되었습니다.")
                .data(data)
                .build()
        );
    }

    @GetMapping("/excuses/my")
    public ResponseEntity<GlobalRes<List<ExcuseRequestRes>>> getMyExcuseRequests(@AuthenticationPrincipal Claims claims) {
        Long userId = Long.parseLong(claims.getSubject());
        List<ExcuseRequestRes> data = academicService.getMyExcuseRequests(userId);

        return ResponseEntity.ok(
            GlobalRes.<List<ExcuseRequestRes>>builder()
                .code("00")
                .message("공결 신청 내역 조회가 완료되었습니다.")
                .data(data)
                .build()
        );
    }

    @PostMapping("/excuses")
    public ResponseEntity<GlobalRes<String>> requestExcuse(
            @AuthenticationPrincipal Claims claims,
            @Valid @RequestBody ExcuseRequestReq req) {
        Long userId = Long.parseLong(claims.getSubject());
        academicService.requestExcuse(userId, req);

        return ResponseEntity.ok(
            GlobalRes.<String>builder()
                .code("00")
                .message("공결 신청이 완료되었습니다.")
                .build()
        );
    }
}
