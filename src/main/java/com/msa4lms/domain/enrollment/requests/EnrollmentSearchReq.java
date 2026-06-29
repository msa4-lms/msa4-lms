package com.msa4lms.domain.enrollment.requests;

public record EnrollmentSearchReq(
    Long studentId,
    Integer year,
    Integer semester
) {
    public EnrollmentSearchReq {
        if (year == null) year = 2026;
        if (semester == null) semester = 1;
    }
}
