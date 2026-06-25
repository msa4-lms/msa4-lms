package com.msa4lms.domain.attendance.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record AttendanceRateRes(
    Long enrollmentId,
    String courseName,
    Integer totalCount,
    Integer attendedCount,
    Double attendanceRate,
    Boolean failTarget
) {}
