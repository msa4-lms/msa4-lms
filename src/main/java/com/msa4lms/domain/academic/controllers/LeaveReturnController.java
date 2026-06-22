package com.msa4lms.domain.academic.controllers;

import com.msa4lms.domain.academic.requests.LeaveReturnProcessReq;
import com.msa4lms.domain.academic.requests.LeaveReturnReq;
import com.msa4lms.domain.academic.responses.LeaveReturnRes;
import com.msa4lms.domain.academic.services.LeaveReturnService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LeaveReturnController {

    private final LeaveReturnService service;

    @PostMapping("/student/academic-requests")
    public ResponseEntity<GlobalRes<Void>> submitRequest(
            @AuthenticationPrincipal Claims claims,
            @RequestBody @Valid LeaveReturnReq req
    ) {
        String role = claims.get("role", String.class);
        if (!"STUDENT".equals(role)) {
            throw new IllegalArgumentException("학생만 신청할 수 있습니다.");
        }
        Long userId = Long.parseLong(claims.getSubject());
        service.submitRequest(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            GlobalRes.<Void>builder()
                .code("00")
                .message("신청이 완료되었습니다.")
                .build()
        );
    }

    @GetMapping("/student/academic-requests/my")
    public ResponseEntity<GlobalRes<List<LeaveReturnRes>>> getMyRequests(
            @AuthenticationPrincipal Claims claims
    ) {
        String role = claims.get("role", String.class);
        if (!"STUDENT".equals(role)) {
            throw new IllegalArgumentException("학생만 조회할 수 있습니다.");
        }
        Long userId = Long.parseLong(claims.getSubject());
        return ResponseEntity.ok(
            GlobalRes.<List<LeaveReturnRes>>builder()
                .code("00")
                .message("조회가 완료되었습니다.")
                .data(service.getMyRequests(userId))
                .build()
        );
    }

    @GetMapping("/professor/academic-requests/pending")
    public ResponseEntity<GlobalRes<List<LeaveReturnRes>>> getPendingRequests(
            @AuthenticationPrincipal Claims claims
    ) {
        String role = claims.get("role", String.class);
        if (!"PROFESSOR".equals(role) && !"ADMIN".equals(role)) {
            throw new IllegalArgumentException("관리자/교수만 조회할 수 있습니다.");
        }
        return ResponseEntity.ok(
            GlobalRes.<List<LeaveReturnRes>>builder()
                .code("00")
                .message("조회가 완료되었습니다.")
                .data(service.getPendingRequests())
                .build()
        );
    }

    @PatchMapping("/professor/academic-requests/{id}/status")
    public ResponseEntity<GlobalRes<Void>> processRequest(
            @AuthenticationPrincipal Claims claims,
            @PathVariable("id") Long id,
            @RequestBody @Valid LeaveReturnProcessReq req
    ) {
        String role = claims.get("role", String.class);
        if (!"PROFESSOR".equals(role) && !"ADMIN".equals(role)) {
            throw new IllegalArgumentException("관리자/교수만 승인/반려할 수 있습니다.");
        }
        service.processRequest(id, req);
        return ResponseEntity.ok(
            GlobalRes.<Void>builder()
                .code("00")
                .message("처리가 완료되었습니다.")
                .build()
        );
    }
}
