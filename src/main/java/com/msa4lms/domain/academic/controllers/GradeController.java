package com.msa4lms.domain.academic.controllers;

import com.msa4lms.domain.academic.entities.Grade;
import com.msa4lms.domain.academic.entities.GradeStatus;
import com.msa4lms.domain.academic.requests.GradeBatchInputReq;
import com.msa4lms.domain.academic.requests.GradeInputReq;
import com.msa4lms.domain.academic.services.GradeService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GradeController {

    private final GradeService gradeService;

    /**
     * 학생의 성적 이의신청 작성
     */
    @PostMapping("/student/grades/{gradeId}/objection")
    public ResponseEntity<GlobalRes<String>> applyObjection(
            @AuthenticationPrincipal Claims claims,
            @PathVariable("gradeId") Long gradeId,
            @RequestBody String objectionReason
    ) {
        Long userId = Long.parseLong(claims.getSubject());
        gradeService.applyObjection(userId, gradeId, objectionReason);

        return ResponseEntity.ok(
            GlobalRes.<String>builder()
                .code("00")
                .message("이의신청이 접수되었습니다.")
                .build()
        );
    }
}
