package com.msa4lms.domain.leaveReturn.controllers;

import com.msa4lms.domain.leaveReturn.requests.LeaveReturnProcessReq;
import com.msa4lms.domain.leaveReturn.responses.LeaveReturnRes;
import com.msa4lms.domain.leaveReturn.services.ProfessorLeaveReturnService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professor/academic-requests")
@RequiredArgsConstructor
public class ProfessorLeaveReturnController {

    private final ProfessorLeaveReturnService service;

    @GetMapping("/pending")
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

    @PatchMapping("/{id}/status")
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
