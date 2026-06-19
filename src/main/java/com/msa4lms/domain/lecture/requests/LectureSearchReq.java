package com.msa4lms.domain.lecture.requests;

public record LectureSearchReq(
    String courseName,
    String professorName,
    String departmentName,
    String collegeName,
    Integer year,
    Integer semester,
    String courseCode,
    Integer page,
    Integer size
) {
    public LectureSearchReq {
        if (page == null) page = 1;
        if (size == null) size = 10;
    }

    public int offset() {
        return (page - 1) * size;
    }
}

