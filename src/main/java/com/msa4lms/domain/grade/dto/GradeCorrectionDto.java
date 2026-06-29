package com.msa4lms.domain.grade.dto;

public record GradeCorrectionDto(
        Long enrollmentId,
        String grade,
        String reason
) {}
