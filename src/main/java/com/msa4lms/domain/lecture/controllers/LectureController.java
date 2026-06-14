package com.msa4lms.domain.lecture.controllers;

import com.msa4lms.domain.lecture.responses.LectureRes;
import com.msa4lms.domain.lecture.services.LectureService;
import com.msa4lms.global.responses.GlobalRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures")
public class LectureController {

    private final LectureService lectureService;

    @GetMapping
    public GlobalRes<List<LectureRes>> getAllLectures() {
        List<LectureRes> lectures = lectureService.getAllLectures();
        return GlobalRes.<List<LectureRes>>builder()
                .code("00")
                .message("강의 목록 조회가 완료되었습니다.")
                .data(lectures)
                .build();
    }
}
