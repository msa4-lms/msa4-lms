package com.msa4lms.domain.academic.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record GradeBatchInputReq(
    @NotEmpty(message = "입력할 성적 목록이 비어 있습니다.")
    List<@Valid GradeInputReq> gradeList
) {}
