package com.msa4lms.domain.attendance.responses;

import java.time.LocalDate;

public record AttendanceRes(
    Long id,
    Long enrollmentId,
    String studentName,
    String courseName,
    LocalDate lectureDate,
    Integer period,
    String status,
    String remarks
) {}
