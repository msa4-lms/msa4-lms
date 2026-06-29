package com.msa4lms.domain.grade.requests;

import com.msa4lms.domain.grade.dto.GradeSaveDto;

import java.util.List;

public record GradeSaveReq(
    List<GradeSaveDto> gradeList
) {}
