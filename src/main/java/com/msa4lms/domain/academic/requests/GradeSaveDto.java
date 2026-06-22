package com.msa4lms.domain.academic.requests;

public record GradeSaveDto(
    Long enrollmentId,
    Double midtermScore,
    Double finalScore,
    Double assignmentScore,
    Double attendanceScore
) {}
