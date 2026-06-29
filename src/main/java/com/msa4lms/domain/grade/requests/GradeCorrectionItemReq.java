package com.msa4lms.domain.grade.requests;

public record GradeCorrectionItemReq(
        Long enrollmentId,
        String grade,
        String reason
) {}
