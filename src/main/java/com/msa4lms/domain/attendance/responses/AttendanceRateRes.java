package com.msa4lms.domain.attendance.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRateRes {
    private Long enrollmentId;
    private String courseName;
    private Integer totalCount;
    private Integer attendedCount;
    private Double attendanceRate;
    private Boolean failTarget;
}
