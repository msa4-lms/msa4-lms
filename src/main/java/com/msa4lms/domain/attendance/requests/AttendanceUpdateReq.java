package com.msa4lms.domain.attendance.requests;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AttendanceUpdateReq(
    @NotNull(message = "수강신청 ID는 필수입니다.")
    Long enrollmentId,

    @NotNull(message = "수업 날짜는 필수입니다.")
    LocalDate lectureDate,

    @NotNull(message = "교시는 필수입니다.")
    Integer period,

    @NotNull(message = "출결 상태는 필수입니다.")
    String status,

    String remarks
) {}
