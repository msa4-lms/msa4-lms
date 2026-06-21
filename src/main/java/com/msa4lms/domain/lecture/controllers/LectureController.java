package com.msa4lms.domain.lecture.controllers;

import com.msa4lms.domain.lecture.requests.LectureCreateReq;
import com.msa4lms.domain.lecture.requests.LectureSearchReq;
import com.msa4lms.domain.lecture.responses.LecturePagedRes;
import com.msa4lms.domain.lecture.responses.LectureRes;
import com.msa4lms.domain.lecture.services.LectureService;
import com.msa4lms.global.responses.GlobalRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("/lectures")
    public ResponseEntity<GlobalRes<LecturePagedRes>> getLectures(@ModelAttribute LectureSearchReq searchReq) {
        LecturePagedRes result = lectureService.searchLectures(searchReq);
        return ResponseEntity.status(200).body(
                GlobalRes.<LecturePagedRes>builder()
                        .code("00")
                        .message("강의 조회가 완료되었습니다.")
                        .data(result)
                        .build()
        );
    }

    @org.springframework.web.bind.annotation.PostMapping("/professor/lectures")
    public ResponseEntity<GlobalRes<String>> createLecture(
            @org.springframework.security.core.annotation.AuthenticationPrincipal io.jsonwebtoken.Claims claims,
            @jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody LectureCreateReq req
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

    @org.springframework.web.bind.annotation.GetMapping("/professor/lectures")
    public ResponseEntity<GlobalRes<java.util.List<LectureRes>>> getProfessorLectures(
            @org.springframework.security.core.annotation.AuthenticationPrincipal io.jsonwebtoken.Claims claims
    ) {
        Long professorId = Long.parseLong(claims.getSubject());
        java.util.List<LectureRes> result = lectureService.getLecturesByProfessor(professorId);
        return ResponseEntity.ok(
                GlobalRes.<java.util.List<LectureRes>>builder()
                        .code("00")
                        .message("담당 강의 조회가 완료되었습니다.")
                        .data(result)
                        .build()
        );
    }
}

