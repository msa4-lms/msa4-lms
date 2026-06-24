package com.msa4lms.domain.grade.requests;

import java.util.List;

public record SaveGradesReq(
    List<GradeSaveDto> gradeList
) {}
