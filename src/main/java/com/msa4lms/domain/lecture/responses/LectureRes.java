package com.msa4lms.domain.lecture.responses;

import lombok.Builder;

@Builder
public record LectureRes(
    Long id,
    String courseCode,
    String courseName,
    Integer credits,
    String professorName,
    String room,
    String schedule,
    Integer capacity,
    Integer year,
    Integer semester
) {}
