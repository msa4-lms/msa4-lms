package com.msa4lms.domain.academic.requests;

import jakarta.validation.constraints.NotBlank;

public record LeaveReturnProcessReq(
    @NotBlank(message = "처리 상태는 필수입니다.") String status,
    String rejectReason
) {}
