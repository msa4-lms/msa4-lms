package com.msa4lms.domain.lecture.controllers;

import com.msa4lms.domain.lecture.requests.LectureCreateReq;
import com.msa4lms.domain.lecture.responses.LectureRes;
import com.msa4lms.domain.lecture.services.ProfessorLectureService;
import com.msa4lms.global.responses.GlobalRes;
import com.msa4lms.global.annotations.LoginUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor/lectures")
public class ProfessorLectureController {

    private final ProfessorLectureService lectureService;

    @GetMapping
    public ResponseEntity<GlobalRes<List<LectureRes>>> getMyLectures(
            @LoginUserId Long professorId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer semester) {
        List<LectureRes> result = lectureService.getLecturesByProfessorId(professorId, year, semester);

        return ResponseEntity.status(200).body(
                GlobalRes.<List<LectureRes>>builder()
                        .code("00")
                        .message("나의 강좌 조회가 완료되었습니다.")
                        .data(result)
                        .build()
        );
    }

    @GetMapping("/past")
    public ResponseEntity<GlobalRes<List<LectureRes>>> getPastLectures(@LoginUserId Long professorId) {
        List<LectureRes> result = lectureService.getPastLecturesByProfessorId(professorId);

        return ResponseEntity.status(200).body(
                GlobalRes.<List<LectureRes>>builder()
                        .code("00")
                        .message("과거 개설 강의 조회가 완료되었습니다.")
                        .data(result)
                        .build()
        );
    }

    @GetMapping("/courses")
    public ResponseEntity<GlobalRes<List<com.msa4lms.domain.lecture.responses.CourseRes>>> getAvailableCourses(@LoginUserId Long professorId) {
        List<com.msa4lms.domain.lecture.responses.CourseRes> result = lectureService.getAvailableCoursesForProfessor(professorId);

        return ResponseEntity.ok(
                GlobalRes.<List<com.msa4lms.domain.lecture.responses.CourseRes>>builder()
                        .code("00")
                        .message("개설 가능 과목 조회가 완료되었습니다.")
                        .data(result)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<GlobalRes<String>> createLecture(
            @LoginUserId Long professorId,
            @Valid @RequestBody LectureCreateReq req
    ) {
        lectureService.createLecture(professorId, req);
        return ResponseEntity.ok(
                GlobalRes.<String>builder()
                        .code("00")
                        .message("강의 개설이 완료되었습니다.")
                        .build()
        );
    }
}
