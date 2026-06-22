package com.msa4lms.domain.lecture.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record LectureCreateReq(
    @NotNull(message = "학기 ID는 필수입니다.")
    Long semesterId,

    @NotNull(message = "과목 ID는 필수입니다.")
    Long courseId,

    @NotBlank(message = "분반 번호는 필수입니다.")
    String sectionNo,

    @NotNull(message = "수강 정원은 필수입니다.")
    @Min(value = 1, message = "수강 정원은 최소 1명 이상이어야 합니다.")
    Integer capacity,

    @NotBlank(message = "강의실은 필수입니다.")
    String classroom,

    @NotNull(message = "중간고사 성적 비율은 필수입니다.")
    @Min(value = 0) @Max(value = 100)
    Integer midtermRatio,

    @NotNull(message = "기말고사 성적 비율은 필수입니다.")
    @Min(value = 0) @Max(value = 100)
    Integer finalRatio,

    @NotNull(message = "과제 성적 비율은 필수입니다.")
    @Min(value = 0) @Max(value = 100)
    Integer assignmentRatio,

    @NotNull(message = "출결 성적 비율은 필수입니다.")
    @Min(value = 0) @Max(value = 100)
    Integer attendanceRatio,

    @NotEmpty(message = "강의 시간표를 최소 1개 이상 등록해 주세요.")
    List<@Valid ScheduleInput> schedules
) {}
