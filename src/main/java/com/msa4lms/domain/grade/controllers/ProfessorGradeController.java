package com.msa4lms.domain.grade.controllers;

import com.msa4lms.domain.grade.requests.ReplyObjectionReq;
import com.msa4lms.domain.grade.requests.SaveGradesReq;
import com.msa4lms.domain.grade.responses.GradeDetailRes;
import com.msa4lms.domain.grade.services.ProfessorGradeService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor/grades")
public class ProfessorGradeController {

    private final ProfessorGradeService professorGradeService;

    @GetMapping("/lectures/{lectureId}")
    public ResponseEntity<GlobalRes<List<GradeDetailRes>>> getGrades(
            @AuthenticationPrincipal Claims claims,
            @PathVariable Long lectureId) {
        Long professorId = Long.parseLong(claims.getSubject());
        List<GradeDetailRes> data = professorGradeService.getGradesForLecture(professorId, lectureId);

        return ResponseEntity.ok(
                GlobalRes.<List<GradeDetailRes>>builder()
                        .code("00")
                        .message("성적 조회가 완료되었습니다.")
                        .data(data)
                        .build()
        );
    }

    @PutMapping("/lectures/{lectureId}")
    public ResponseEntity<GlobalRes<Void>> saveGrades(
            @AuthenticationPrincipal Claims claims,
            @PathVariable Long lectureId,
            @RequestBody SaveGradesReq req) {
        Long professorId = Long.parseLong(claims.getSubject());
        professorGradeService.saveGrades(professorId, lectureId, req);

        return ResponseEntity.ok(
                GlobalRes.<Void>builder()
                        .code("00")
                        .message("성적 임시저장이 완료되었습니다.")
                        .build()
        );
    }

    @PatchMapping("/lectures/{lectureId}/status")
    public ResponseEntity<GlobalRes<Void>> updateGradesStatus(
            @AuthenticationPrincipal Claims claims,
            @PathVariable Long lectureId,
            @RequestParam String status) {
        Long professorId = Long.parseLong(claims.getSubject());
        professorGradeService.updateGradesStatus(professorId, lectureId, status);

        return ResponseEntity.ok(
                GlobalRes.<Void>builder()
                        .code("00")
                        .message("성적 상태 변경이 완료되었습니다.")
                        .build()
        );
    }

    @PatchMapping("/{gradeId}/objection")
    public ResponseEntity<GlobalRes<Void>> replyObjection(
            @AuthenticationPrincipal Claims claims,
            @PathVariable Long gradeId,
            @RequestParam Boolean approve,
            @RequestParam String reply,
            @RequestBody(required = false) ReplyObjectionReq newScoresBody) {
        Long professorId = Long.parseLong(claims.getSubject());
        
        ReplyObjectionReq req = new ReplyObjectionReq(approve, reply, newScoresBody != null ? newScoresBody.newScores() : null);
        
        professorGradeService.replyObjection(professorId, gradeId, req);

        return ResponseEntity.ok(
                GlobalRes.<Void>builder()
                        .code("00")
                        .message("이의신청 처리가 완료되었습니다.")
                        .build()
        );
    }
}
