package com.msa4lms.domain.academic.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcuseRequestRes {
    private Long id;
    private Long enrollmentId;
    private String courseName;
    private String studentName;
    private String lectureDate;
    private Integer period;
    private String reason;
    private String status;
    private String rejectReason;
}
