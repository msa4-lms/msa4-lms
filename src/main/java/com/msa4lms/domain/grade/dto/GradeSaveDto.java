package com.msa4lms.domain.grade.dto;

public record GradeSaveDto(
        Long enrollmentId,
        Double midtermScore,
        Double finalScore,
        Double assignmentScore,
        Double attendanceScore,
        String grade
) {}
