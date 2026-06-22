package com.msa4lms.domain.academic.requests;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record GradeInputReq(
    @NotNull(message = "수강신청 ID는 필수입니다.")
    Long enrollmentId,

    @NotNull(message = "중간고사 점수는 필수입니다.")
    @DecimalMin(value = "0.00", message = "성적은 0점 이상이어야 합니다.")
    @DecimalMax(value = "100.00", message = "성적은 100점 이하여야 합니다.")
    BigDecimal midtermScore,

    @NotNull(message = "기말고사 점수는 필수입니다.")
    @DecimalMin(value = "0.00", message = "성적은 0점 이상이어야 합니다.")
    @DecimalMax(value = "100.00", message = "성적은 100점 이하여야 합니다.")
    BigDecimal finalScore,

    @NotNull(message = "과제 점수는 필수입니다.")
    @DecimalMin(value = "0.00", message = "성적은 0점 이상이어야 합니다.")
    @DecimalMax(value = "100.00", message = "성적은 100점 이하여야 합니다.")
    BigDecimal assignmentScore,

    @NotNull(message = "출결 점수는 필수입니다.")
    @DecimalMin(value = "0.00", message = "성적은 0점 이상이어야 합니다.")
    @DecimalMax(value = "100.00", message = "성적은 100점 이하여야 합니다.")
    BigDecimal attendanceScore
) {}
