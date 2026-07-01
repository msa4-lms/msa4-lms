package com.msa4lms.domain.lecture.responses;

import java.util.List;

public record LecturePagedRes(
    List<LectureRes> lectures,
    long totalCount,
    int page,
    int size
) {
}
