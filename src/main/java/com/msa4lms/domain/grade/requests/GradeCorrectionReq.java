package com.msa4lms.domain.grade.requests;

import com.msa4lms.domain.grade.dto.GradeCorrectionDto;

import java.util.List;

public record GradeCorrectionReq(
        List<GradeCorrectionDto> correctionList
) {}
