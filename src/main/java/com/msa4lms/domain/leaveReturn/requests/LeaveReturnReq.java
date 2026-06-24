package com.msa4lms.domain.leaveReturn.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LeaveReturnReq(
    @NotBlank(message = "신청 유형은 필수입니다.") String requestType,
    @NotBlank(message = "사유를 입력해주세요.") String reason,
    @NotNull(message = "적용 학년도를 입력해주세요.") Integer targetYear,
    @NotNull(message = "적용 학기를 입력해주세요.") Integer targetSemester,
    Integer returnYear,
    Integer returnSemester
) {}
