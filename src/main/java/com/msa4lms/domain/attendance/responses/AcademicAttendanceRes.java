package com.msa4lms.domain.attendance.responses;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicAttendanceRes {
    private String courseName;
    private String lectureDate;
    private Integer period;
    private String status;
}
