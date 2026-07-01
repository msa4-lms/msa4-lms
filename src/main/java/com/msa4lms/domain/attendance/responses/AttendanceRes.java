package com.msa4lms.domain.attendance.responses;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRes {
    private Long id;
    private Long enrollmentId;
    private String studentName;
    private String courseName;
    private LocalDate lectureDate;
    private Integer period;
    private String status;
    private String remarks;
}
