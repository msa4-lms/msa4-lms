package com.msa4lms.domain.enrollment.requests;

/**
 * 내 수강 내역 조회 요청을 위한 DTO입니다.
 */
public record EnrollmentReq(
    Long studentId,
    Integer year,
    Integer semester
) {
    public EnrollmentReq {
        if (year == null) year = 2026;
        if (semester == null) semester = 1;
    }
}
