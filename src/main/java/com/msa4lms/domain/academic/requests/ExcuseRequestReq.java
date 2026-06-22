package com.msa4lms.domain.academic.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ExcuseRequestReq(
    @NotNull(message = "수강신청 ID는 필수입니다.")
    Long enrollmentId,

    @NotNull(message = "강의일자는 필수입니다.")
    LocalDate lectureDate,

    @NotNull(message = "교시는 필수입니다.")
    Integer period,

    @NotBlank(message = "공결 사유는 필수입니다.")
    String reason
) {}
