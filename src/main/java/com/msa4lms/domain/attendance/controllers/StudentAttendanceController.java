package com.msa4lms.domain.attendance.controllers;

import com.msa4lms.domain.attendance.responses.AttendanceRes;
import com.msa4lms.domain.attendance.responses.AcademicAttendanceRes;
import com.msa4lms.domain.attendance.responses.AttendanceRateRes;
import com.msa4lms.domain.attendance.requests.ExcuseApplyReq;
import com.msa4lms.domain.attendance.responses.ExcuseRequestRes;
import com.msa4lms.domain.attendance.responses.ExcuseAttachmentFile;
import com.msa4lms.domain.attendance.services.StudentAttendanceService;
import com.msa4lms.global.annotations.LoginUserId;
import com.msa4lms.global.responses.GlobalRes;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ContentDisposition;
import org.springframework.core.io.Resource;
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
    private final StudentAttendanceService attendanceService;

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
    public ResponseEntity<GlobalRes<List<AcademicAttendanceRes>>> getAttendance(@LoginUserId Long userId) {
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
            @LoginUserId Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer semester) {
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
    public ResponseEntity<GlobalRes<List<ExcuseRequestRes>>> getMyExcuseRequests(@LoginUserId Long userId) {
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
            @LoginUserId Long userId,
            @Valid @RequestBody ExcuseApplyReq req) {
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
            @LoginUserId Long userId,
            @Valid @ModelAttribute ExcuseApplyReq req,
            @RequestPart(name = "attachment", required = false) MultipartFile attachment) {
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
            @LoginUserId Long studentId,
            @PathVariable long requestId) {
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
