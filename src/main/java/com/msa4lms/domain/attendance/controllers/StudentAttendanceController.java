package com.msa4lms.domain.attendance.controllers;

import com.msa4lms.domain.attendance.responses.AttendanceRes;
import com.msa4lms.domain.attendance.services.AttendanceService;
import com.msa4lms.global.responses.GlobalRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
