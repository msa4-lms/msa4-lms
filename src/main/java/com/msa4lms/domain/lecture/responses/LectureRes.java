package com.msa4lms.domain.lecture.responses;

import lombok.Builder;

@Builder
public record LectureRes(
    Long id,
    String courseCode,
    String courseName,
    Integer credits,
    Integer targetGrade,
    String departmentName,
    String professorName,
    String classroom,
    String schedule,
    Integer capacity,
    Integer currentEnrollment,
    Integer academicYear,
    String term,
    Integer midtermRatio,
    Integer finalRatio,
    Integer assignmentRatio,
    Integer attendanceRatio
) {}
