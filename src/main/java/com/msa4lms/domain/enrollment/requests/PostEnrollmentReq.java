package com.msa4lms.domain.enrollment.requests;

import jakarta.validation.constraints.NotNull;

public record PostEnrollmentReq(
    @NotNull(message = "학생 ID는 필수입니다.")
    Long studentId,
    
    @NotNull(message = "강의 ID는 필수입니다.")
    Long lectureId
) {}
