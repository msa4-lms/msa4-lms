package com.msa4lms.domain.dashboard.responses;

public record AcademicScheduleRes(
        Long id
        , String title
        , String content
        , String startDate
        , String endDate
        , String targetRole
) {
}
