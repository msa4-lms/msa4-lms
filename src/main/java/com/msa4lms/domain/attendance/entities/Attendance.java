package com.msa4lms.domain.attendance.entities;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class Attendance {
    private Long id;
    private Long enrollmentId;
    private LocalDate lectureDate;
    private Integer period;
    private String status; // PRESENT, ABSENT, LATE, EXCUSED
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
