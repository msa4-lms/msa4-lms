package com.msa4lms.domain.academic.requests;

import java.util.List;

public record SaveGradesReq(
    List<GradeSaveDto> gradeList
) {}
