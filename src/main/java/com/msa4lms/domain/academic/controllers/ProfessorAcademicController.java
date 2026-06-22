package com.msa4lms.domain.academic.controllers;

import com.msa4lms.domain.academic.requests.ExcuseDecisionReq;
import com.msa4lms.domain.academic.responses.ExcuseRequestRes;
import com.msa4lms.domain.academic.services.AcademicService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor/academic")
public class ProfessorAcademicController {
    private final AcademicService academicService;

    @GetMapping("/excuses/pending")
    public ResponseEntity<GlobalRes<List<ExcuseRequestRes>>> getPendingExcuseRequests(
            @AuthenticationPrincipal Claims claims) {
        Long professorId = Long.parseLong(claims.getSubject());
        List<ExcuseRequestRes> data = academicService.getPendingExcuseRequests(professorId);

        return ResponseEntity.ok(
            GlobalRes.<List<ExcuseRequestRes>>builder()
                .code("00")
                .message("공결 승인 대기 목록 조회가 완료되었습니다.")
                .data(data)
                .build()
        );
    }

    @PatchMapping("/excuses/{requestId}")
    public ResponseEntity<GlobalRes<String>> decideExcuseRequest(
            @AuthenticationPrincipal Claims claims,
            @PathVariable long requestId,
            @Valid @RequestBody ExcuseDecisionReq req) {
        Long professorId = Long.parseLong(claims.getSubject());
        academicService.decideExcuseRequest(professorId, requestId, req);

        return ResponseEntity.ok(
            GlobalRes.<String>builder()
                .code("00")
                .message("공결 신청 처리가 완료되었습니다.")
                .build()
        );
    }
}
