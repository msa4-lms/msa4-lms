package com.msa4lms.domain.grade.requests;

public record GradeSaveItemReq(
        Long enrollmentId,
        Double midtermScore,
        Double finalScore,
        Double assignmentScore,
        Double attendanceScore,
        String grade
) {}
