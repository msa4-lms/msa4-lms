package com.msa4lms.domain.grade.controllers;

import com.msa4lms.domain.grade.requests.GradeSaveReq;
import com.msa4lms.domain.grade.responses.GradeDetailRes;
import com.msa4lms.domain.grade.responses.ProfessorLectureRes;
import com.msa4lms.domain.grade.services.ProfessorGradeService;
import com.msa4lms.global.annotations.LoginUserId;
import com.msa4lms.global.responses.GlobalRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor/grades")
public class ProfessorGradeController {

    private final ProfessorGradeService professorGradeService;

    @GetMapping("/lectures/{lectureId}")
    public ResponseEntity<GlobalRes<List<GradeDetailRes>>> getGrades(
            @LoginUserId Long professorId,
            @PathVariable Long lectureId) {
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
            @LoginUserId Long professorId,
            @PathVariable Long lectureId,
            @RequestBody GradeSaveReq req) {
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
            @LoginUserId Long professorId,
            @PathVariable Long lectureId,
            @RequestParam String status) {
        professorGradeService.updateGradesStatus(professorId, lectureId, status);

        return ResponseEntity.ok(
                GlobalRes.<Void>builder()
                        .code("00")
                        .message("성적 상태 변경이 완료되었습니다.")
                        .build()
        );
    }

    @GetMapping("/lectures")
    public ResponseEntity<GlobalRes<List<ProfessorLectureRes>>> getLecures(
            @LoginUserId Long professorId
    ) {
        List<ProfessorLectureRes> data =
                professorGradeService.getLecture(professorId);

        return ResponseEntity.ok(
                GlobalRes.<List<ProfessorLectureRes>>builder()
                        .code("00")
                        .message("강의 조회 완료")
                        .data(data)
                        .build()
        );
    }


}
