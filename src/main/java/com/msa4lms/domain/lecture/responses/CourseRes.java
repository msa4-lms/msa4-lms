package com.msa4lms.domain.lecture.responses;

public record CourseRes(
    Long id,
    String code,
    String name,
    Integer credits,
    Integer targetGrade,
    String completionType
) {}
