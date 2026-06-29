package com.msa4lms.domain.attendance.controllers;

import com.msa4lms.domain.attendance.requests.AttendanceUpdateReq;
import com.msa4lms.domain.attendance.responses.AttendanceRes;
import com.msa4lms.domain.attendance.services.StudentAttendanceService;
import com.msa4lms.domain.attendance.requests.ExcuseDecisionReq;
import com.msa4lms.domain.attendance.responses.ExcuseRequestRes;
import com.msa4lms.domain.attendance.responses.ExcuseAttachmentFile;
import com.msa4lms.global.annotations.LoginUserId;
import com.msa4lms.global.responses.GlobalRes;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ContentDisposition;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import com.msa4lms.domain.attendance.services.ProfessorAttendanceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor/attendances")
public class ProfessorAttendanceController {
    private final ProfessorAttendanceService attendanceService;

    /**
     * 출결 정보 등록
     */
    @PostMapping
    public ResponseEntity<GlobalRes<Void>> saveAttendance(@RequestBody @Valid AttendanceUpdateReq req) {
        attendanceService.saveAttendance(req);
        return ResponseEntity.ok(
                GlobalRes.<Void>builder()
                        .code("00")
                        .message("출결 정보가 등록되었습니다.")
                        .build()
        );
    }
    /**
     * 특정 강의/날짜의 전체 출결 내역 조회 (교수용)
     */
    @GetMapping("/lecture/{lectureId}")
    public ResponseEntity<GlobalRes<List<AttendanceRes>>> getLectureAttendances(
            @PathVariable Long lectureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceRes> data = attendanceService.getLectureAttendances(lectureId, date);
        return ResponseEntity.ok(
                GlobalRes.<List<AttendanceRes>>builder()
                        .code("00")
                        .message("강의 출결 내역 조회가 완료되었습니다.")
                        .data(data)
                        .build()
        );
    }


    /**
     * 출결 정보 수정
     */
    @PatchMapping("/{id}")
    public ResponseEntity<GlobalRes<Void>> updateAttendance(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String remarks) {
        attendanceService.updateAttendance(id, status, remarks);
        return ResponseEntity.ok(
                GlobalRes.<Void>builder()
                        .code("00")
                        .message("출결 정보가 수정되었습니다.")
                        .build()
        );
    }

    @GetMapping("/excuses/pending")
    public ResponseEntity<GlobalRes<List<ExcuseRequestRes>>> getPendingExcuseRequests(
            @LoginUserId Long professorId) {
        List<ExcuseRequestRes> data = attendanceService.getPendingExcuseRequests(professorId);

        return ResponseEntity.ok(
            GlobalRes.<List<ExcuseRequestRes>>builder()
                .code("00")
                .message("공결 승인 대기 목록 조회가 완료되었습니다.")
                .data(data)
                .build()
        );
    }

    @GetMapping("/excuses")
    public ResponseEntity<GlobalRes<List<ExcuseRequestRes>>> getExcuseRequests(
            @LoginUserId Long professorId) {
        List<ExcuseRequestRes> data = attendanceService.getProfessorExcuseRequests(professorId);

        return ResponseEntity.ok(
            GlobalRes.<List<ExcuseRequestRes>>builder()
                .code("00")
                .message("공결 신청 목록 조회가 완료되었습니다.")
                .data(data)
                .build()
        );
    }

    @PatchMapping("/excuses/{requestId}")
    public ResponseEntity<GlobalRes<String>> decideExcuseRequest(
            @LoginUserId Long professorId,
            @PathVariable long requestId,
            @Valid @RequestBody ExcuseDecisionReq req) {
        attendanceService.decideExcuseRequest(professorId, requestId, req);

        return ResponseEntity.ok(
            GlobalRes.<String>builder()
                .code("00")
                .message("공결 신청 처리가 완료되었습니다.")
                .build()
        );
    }

    @GetMapping("/excuses/{requestId}/attachment")
    public ResponseEntity<Resource> getExcuseAttachment(
            @LoginUserId Long professorId,
            @PathVariable long requestId) {
        ExcuseAttachmentFile file = attendanceService.getExcuseAttachment(professorId, requestId);
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
