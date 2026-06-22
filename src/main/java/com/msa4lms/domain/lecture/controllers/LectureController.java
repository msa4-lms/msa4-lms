package com.msa4lms.domain.lecture.controllers;

import com.msa4lms.domain.lecture.requests.LectureCreateReq;
import com.msa4lms.domain.lecture.requests.LectureSearchReq;
import com.msa4lms.domain.lecture.responses.LecturePagedRes;
import com.msa4lms.domain.lecture.responses.LectureRes;
import com.msa4lms.domain.lecture.responses.CollegeWithDepartmentsRes;
import com.msa4lms.domain.lecture.services.LectureService;
import com.msa4lms.global.responses.GlobalRes;
import java.util.List;
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



    @GetMapping("/lectures/colleges")
    public ResponseEntity<GlobalRes<List<CollegeWithDepartmentsRes>>> getCollegesWithDepartments() {
        List<CollegeWithDepartmentsRes> result = lectureService.getCollegesWithDepartments();
        return ResponseEntity.status(200).body(
                GlobalRes.<List<CollegeWithDepartmentsRes>>builder()
                        .code("00")
                        .message("단과대 및 학과 조회가 완료되었습니다.")
                        .data(result)
                        .build()
        );
    }
}

