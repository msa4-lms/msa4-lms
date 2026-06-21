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
     * 교수의 특정 강좌 수강생 성적 조회
     */
    @GetMapping("/professor/grades/lectures/{lectureId}")
    public ResponseEntity<GlobalRes<List<Grade>>> getGrades(
            @AuthenticationPrincipal Claims claims,
            @PathVariable("lectureId") Long lectureId
    ) {
        Long userId = Long.parseLong(claims.getSubject());
        List<Grade> data = gradeService.getGradesByLecture(userId, lectureId);

        return ResponseEntity.ok(
            GlobalRes.<List<Grade>>builder()
                .code("00")
                .message("성적 조회가 완료되었습니다.")
                .data(data)
                .build()
        );
    }

    /**
     * 교수의 수강생 성적 일괄 입력 및 수정
     */
    @PutMapping("/professor/grades/lectures/{lectureId}")
    public ResponseEntity<GlobalRes<String>> saveGrades(
            @AuthenticationPrincipal Claims claims,
            @PathVariable("lectureId") Long lectureId,
            @Valid @RequestBody GradeBatchInputReq req
    ) {
        Long userId = Long.parseLong(claims.getSubject());
        gradeService.saveGrades(userId, lectureId, req);

        return ResponseEntity.ok(
            GlobalRes.<String>builder()
                .code("00")
                .message("성적이 임시저장(DRAFT)되었습니다.")
                .build()
        );
    }

    /**
     * 교수의 강좌 성적 상태 일괄 변경
     */
    @PatchMapping("/professor/grades/lectures/{lectureId}/status")
    public ResponseEntity<GlobalRes<String>> updateGradesStatus(
            @AuthenticationPrincipal Claims claims,
            @PathVariable("lectureId") Long lectureId,
            @RequestParam("status") GradeStatus status
    ) {
        Long userId = Long.parseLong(claims.getSubject());
        gradeService.updateLectureGradesStatus(userId, lectureId, status);

        return ResponseEntity.ok(
            GlobalRes.<String>builder()
                .code("00")
                .message("성적 상태가 " + status.name() + "(으)로 변경되었습니다.")
                .build()
        );
    }

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

    /**
     * 교수의 이의신청 답변 및 상태 변경
     */
    @PatchMapping("/professor/grades/{gradeId}/objection")
    public ResponseEntity<GlobalRes<String>> replyObjection(
            @AuthenticationPrincipal Claims claims,
            @PathVariable("gradeId") Long gradeId,
            @RequestParam("approve") boolean approve,
            @RequestParam("reply") String reply,
            @Valid @RequestBody(required = false) GradeInputReq newScores
    ) {
        Long userId = Long.parseLong(claims.getSubject());
        gradeService.replyObjection(userId, gradeId, reply, approve, newScores);

        return ResponseEntity.ok(
            GlobalRes.<String>builder()
                .code("00")
                .message("이의신청 처리가 완료되었습니다.")
                .build()
        );
    }
}
