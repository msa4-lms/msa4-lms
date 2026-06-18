package com.msa4lms.domain.attendance.controllers;

import com.msa4lms.domain.attendance.requests.PostAttendanceReq;
import com.msa4lms.domain.attendance.responses.AttendanceRes;
import com.msa4lms.domain.attendance.services.AttendanceService;
import com.msa4lms.global.responses.GlobalRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor/attendances")
public class ProfessorAttendanceController {
    private final AttendanceService attendanceService;

    /**
     * 출결 정보 등록
     */
    @PostMapping
    public ResponseEntity<GlobalRes<Void>> saveAttendance(@RequestBody @Valid PostAttendanceReq req) {
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

}
