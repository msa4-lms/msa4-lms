package com.msa4lms.domain.attendance.requests;

import jakarta.validation.constraints.NotBlank;

public record ExcuseDecisionReq(
    @NotBlank(message = "승인 상태는 필수입니다.")
    String status,

    String rejectReason
) {}
