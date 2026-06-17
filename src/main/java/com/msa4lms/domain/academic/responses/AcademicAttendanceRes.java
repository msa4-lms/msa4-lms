package com.msa4lms.domain.academic.responses;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicAttendanceRes {
    private String courseName;
    private String lectureDate;
    private String status;
}
