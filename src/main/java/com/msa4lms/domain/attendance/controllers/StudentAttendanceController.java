package com.msa4lms.domain.attendance.controllers;

import com.msa4lms.domain.attendance.responses.AttendanceRes;
import com.msa4lms.domain.attendance.responses.AcademicAttendanceRes;
import com.msa4lms.domain.attendance.responses.AttendanceRateRes;
import com.msa4lms.domain.attendance.requests.ExcuseRequestReq;
import com.msa4lms.domain.attendance.responses.ExcuseRequestRes;
import com.msa4lms.domain.attendance.responses.ExcuseAttachmentFile;
import com.msa4lms.domain.attendance.services.AttendanceService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ContentDisposition;
import org.springframework.core.io.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/student/attendances")
@RequiredArgsConstructor
public class StudentAttendanceController {
    private final AttendanceService attendanceService;

    /**
     * 특정 수강신청의 출결 내역 조회 (학생용)
     */
    @GetMapping("/my/{enrollmentId}")
    public ResponseEntity<GlobalRes<List<AttendanceRes>>> getMyAttendances(@PathVariable Long enrollmentId) {
        List<AttendanceRes> data = attendanceService.getMyAttendances(enrollmentId);
        return ResponseEntity.ok(
                GlobalRes.<List<AttendanceRes>>builder()
                        .code("00")
                        .message("출결 내역 조회가 완료되었습니다.")
                        .data(data)
                        .build()
        );
    }

    @GetMapping("/attendance")
    public ResponseEntity<GlobalRes<List<AcademicAttendanceRes>>> getAttendance(@AuthenticationPrincipal Claims claims) {
        Long userId = Long.parseLong(claims.getSubject());
        List<AcademicAttendanceRes> data = attendanceService.getAttendance(userId);

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
        List<AttendanceRateRes> data = attendanceService.getAttendanceRates(userId, year, semester);

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
        List<ExcuseRequestRes> data = attendanceService.getMyExcuseRequests(userId);

        return ResponseEntity.ok(
            GlobalRes.<List<ExcuseRequestRes>>builder()
                .code("00")
                .message("공결 신청 내역 조회가 완료되었습니다.")
                .data(data)
                .build()
        );
    }

    @PostMapping(value = "/excuses", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GlobalRes<String>> requestExcuse(
            @AuthenticationPrincipal Claims claims,
            @Valid @RequestBody ExcuseRequestReq req) {
        Long userId = Long.parseLong(claims.getSubject());
        attendanceService.requestExcuse(userId, req);

        return ResponseEntity.ok(
            GlobalRes.<String>builder()
                .code("00")
                .message("공결 신청이 완료되었습니다.")
                .build()
        );
    }

    @PostMapping(value = "/excuses", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalRes<String>> requestExcuseWithAttachment(
            @AuthenticationPrincipal Claims claims,
            @Valid @ModelAttribute ExcuseRequestReq req,
            @RequestPart(name = "attachment", required = false) MultipartFile attachment) {
        Long userId = Long.parseLong(claims.getSubject());
        attendanceService.requestExcuse(userId, req, attachment);

        return ResponseEntity.ok(
            GlobalRes.<String>builder()
                .code("00")
                .message("공결 신청이 완료되었습니다.")
                .build()
        );
    }

    @GetMapping("/excuses/{requestId}/attachment")
    public ResponseEntity<Resource> getExcuseAttachment(
            @AuthenticationPrincipal Claims claims,
            @PathVariable long requestId) {
        Long studentId = Long.parseLong(claims.getSubject());
        ExcuseAttachmentFile file = attendanceService.getStudentExcuseAttachment(studentId, requestId);
        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(file.contentType());
        } catch (IllegalArgumentException e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }
        boolean inline = MediaType.APPLICATION_PDF.includes(mediaType)
                || "image".equalsIgnoreCase(mediaType.getType());
        ContentDisposition disposition = (inline
                ? ContentDisposition.inline()
                : ContentDisposition.attachment())
                .filename(file.originalName(), java.nio.charset.StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(file.resource());
    }
}
