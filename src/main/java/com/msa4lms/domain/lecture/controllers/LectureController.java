package com.msa4lms.domain.lecture.controllers;

import com.msa4lms.domain.lecture.requests.LectureSearchReq;
import com.msa4lms.domain.lecture.responses.LecturePagedRes;
import com.msa4lms.domain.lecture.services.LectureService;
import com.msa4lms.global.responses.GlobalRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures")
public class LectureController {

    private final LectureService lectureService;

    @GetMapping
    public GlobalRes<LecturePagedRes> getLectures(@ModelAttribute LectureSearchReq searchReq) {
        LecturePagedRes result = lectureService.searchLectures(searchReq);
        return GlobalRes.<LecturePagedRes>builder()
                .code("00")
                .message("강의 조회가 완료되었습니다.")
                .data(result)
                .build();
    }
}
