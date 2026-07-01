package com.msa4lms.domain.grade.responses;

import lombok.*;

@Builder
public record SemesterGradeRes(
    Long id,
    Integer year,
    Integer semester,
    String courseName,
    String courseCode,
    Integer credits,
    String grade,
    Double gradePoint,
    String status,
    Double attendanceRate
) {}
