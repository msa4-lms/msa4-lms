package com.msa4lms.domain.lecture.responses;

import lombok.Builder;

@Builder
public record LectureRes(
    Long id,
    String courseCode,
    String courseName,
    Integer credits,
    String departmentName,
    String professorName,
    String classroom,
    String schedule,
    Integer capacity,
    Integer academicYear,
    String term
) {}
