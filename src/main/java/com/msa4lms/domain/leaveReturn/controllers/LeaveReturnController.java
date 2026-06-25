package com.msa4lms.domain.leaveReturn.controllers;

import com.msa4lms.domain.leaveReturn.requests.LeaveReturnReq;
import com.msa4lms.domain.leaveReturn.responses.LeaveReturnRes;
import com.msa4lms.domain.leaveReturn.services.LeaveReturnService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LeaveReturnController {

    private final LeaveReturnService service;

    @PostMapping(value = "/student/academic-requests", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalRes<Void>> submitRequest(
            @AuthenticationPrincipal Claims claims,
            @RequestPart("request") @Valid LeaveReturnReq req,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        String role = claims.get("role", String.class);
        if (!"STUDENT".equals(role)) {
            throw new IllegalArgumentException("학생만 신청할 수 있습니다.");
        }
        Long userId = Long.parseLong(claims.getSubject());
        service.submitRequest(userId, req, file);
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
}
