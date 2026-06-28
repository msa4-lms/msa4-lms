package com.msa4lms.domain.enrollment.requests;

import jakarta.validation.constraints.NotNull;

/**
 * 수강 신청 요청을 위한 DTO입니다.
 * 학생 ID는 인증 정보(JWT)에서 직접 추출하므로, 클라이언트에서는 강의 ID만 전달합니다.
 */
public record EnrollmentCreateReq(
    @NotNull(message = "강의 ID는 필수입니다.")
    Long lectureId
) {}
