package com.msa4lms.domain.grade.requests;

public record GradeSaveDto(
    Long enrollmentId,
    Double midtermScore,
    Double finalScore,
    Double assignmentScore,
    Double attendanceScore
) {}
