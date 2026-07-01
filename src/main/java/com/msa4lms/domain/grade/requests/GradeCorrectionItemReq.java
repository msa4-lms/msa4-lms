package com.msa4lms.domain.grade.requests;

public record GradeCorrectionItemReq(
        Long enrollmentId,
        Double midtermScore,
        Double finalScore,
        Double assignmentScore,
        Double attendanceScore,
        String grade,
        String reason
) {}
