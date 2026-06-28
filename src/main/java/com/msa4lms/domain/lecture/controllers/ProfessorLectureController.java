package com.msa4lms.domain.lecture.controllers;

import com.msa4lms.domain.lecture.requests.LectureCreateReq;
import com.msa4lms.domain.lecture.responses.LectureRes;
import com.msa4lms.domain.lecture.services.ProfessorLectureService;
import com.msa4lms.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // ProfessorLectureController는 서비스를 호출하여 각각의 원하는 명령을 수행시킨다.
    private final ProfessorLectureService lectureService;  // lectureService 호출


    @GetMapping
    public ResponseEntity<GlobalRes<List<LectureRes>>> getMyLectures(
            @AuthenticationPrincipal Claims claims,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer semester) {
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

    @PostMapping
    public ResponseEntity<GlobalRes<String>> createLecture(
            @AuthenticationPrincipal Claims claims,
            @Valid @RequestBody LectureCreateReq req
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
