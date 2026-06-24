package com.msa4lms.domain.lecture.controllers;

import com.msa4lms.domain.lecture.responses.LectureRes;
import com.msa4lms.domain.lecture.services.LectureService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor/lectures")
public class ProfessorLectureController {

    private final LectureService lectureService;

    @GetMapping
    public ResponseEntity<GlobalRes<List<LectureRes>>> getMyLectures(
            @AuthenticationPrincipal Claims claims,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Integer year,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Integer semester) {
        Long professorId = Long.parseLong(claims.getSubject());
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
    public ResponseEntity<GlobalRes<List<LectureRes>>> getPastLectures(@AuthenticationPrincipal Claims claims) {
        Long professorId = Long.parseLong(claims.getSubject());
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
    public ResponseEntity<GlobalRes<List<com.msa4lms.domain.lecture.responses.CourseRes>>> getAvailableCourses(@AuthenticationPrincipal Claims claims) {
        Long professorId = Long.parseLong(claims.getSubject());
        List<com.msa4lms.domain.lecture.responses.CourseRes> result = lectureService.getAvailableCoursesForProfessor(professorId);

        return ResponseEntity.ok(
                GlobalRes.<List<com.msa4lms.domain.lecture.responses.CourseRes>>builder()
                        .code("00")
                        .message("개설 가능 과목 조회가 완료되었습니다.")
                        .data(result)
                        .build()
        );
    }

    @org.springframework.web.bind.annotation.PostMapping
    public ResponseEntity<GlobalRes<String>> createLecture(
            @org.springframework.security.core.annotation.AuthenticationPrincipal io.jsonwebtoken.Claims claims,
            @jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody com.msa4lms.domain.lecture.requests.LectureCreateReq req
    ) {
        Long professorId = Long.parseLong(claims.getSubject());
        lectureService.createLecture(professorId, req);
        return ResponseEntity.ok(
                GlobalRes.<String>builder()
                        .code("00")
                        .message("강의 개설이 완료되었습니다.")
                        .build()
        );
    }
}
