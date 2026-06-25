package com.msa4lms.domain.grade.responses;

import lombok.Builder;

@Builder
public record GradeDetailRes(
    Long id,
    Long enrollmentId,
    String studentName,
    String studentLoginId,
    Double midtermScore,
    Double finalScore,
    Double assignmentScore,
    Double attendanceScore,
    String status,
    String objectionReply,
    String grade
) {}
