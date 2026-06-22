package com.msa4lms.domain.enrollment.responses;

public record LectureInfoForEnrollment(
    int academicYear,
    String term,
    int credits
) {}
