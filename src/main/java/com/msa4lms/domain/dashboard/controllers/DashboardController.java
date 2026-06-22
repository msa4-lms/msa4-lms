package com.msa4lms.domain.dashboard.controllers;

import com.msa4lms.domain.dashboard.responses.AcademicScheduleRes;
import com.msa4lms.domain.dashboard.responses.NoticeRes;
import com.msa4lms.domain.dashboard.services.DashboardService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/schedules")
    public ResponseEntity<GlobalRes<List<AcademicScheduleRes>>> getSchedules(
            @AuthenticationPrincipal Claims claims
    ) {
        String role = claims.get("role", String.class);

        List<AcademicScheduleRes> schedules =
                dashboardService.getSchedules(role);

        return ResponseEntity.ok(
                GlobalRes.<List<AcademicScheduleRes>>builder()
                        .code("00")
                        .message("스케줄 조회 완료")
                        .data(schedules)
                        .build()
        );
    }

    @GetMapping("/notices")
    public ResponseEntity<GlobalRes<List<NoticeRes>>> getNotices(
            @AuthenticationPrincipal Claims claims
    ){
        String role = claims.get("role",String.class);

        List<NoticeRes> notices =
                dashboardService.getNotices(role);

        return ResponseEntity.ok(
                GlobalRes.<List<NoticeRes>>builder()
                        .code("00")
                        .message("공지 조회 완료")
                        .data(notices)
                        .build()
        );
    }
}
