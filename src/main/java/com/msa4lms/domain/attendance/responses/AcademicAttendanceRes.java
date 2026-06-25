package com.msa4lms.domain.attendance.responses;

import lombok.*;

@Builder
public record AcademicAttendanceRes(
    String courseName,
    String lectureDate,
    Integer period,
    String status
) {}
