package com.msa4lms.domain.lecture.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ScheduleInput(
    @NotBlank(message = "요일은 필수입니다.")
    String dayOfWeek, // MON, TUE, WED, THU, FRI

    @NotNull(message = "시작 교시는 필수입니다.")
    @Min(value = 1) @Max(value = 9)
    Integer startPeriod,

    @NotNull(message = "종료 교시는 필수입니다.")
    @Min(value = 1) @Max(value = 9)
    Integer endPeriod
) {}
