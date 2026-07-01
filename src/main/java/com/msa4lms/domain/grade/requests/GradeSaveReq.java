package com.msa4lms.domain.grade.requests;

import java.util.List;

public record GradeSaveReq(
        List<GradeSaveItemReq> gradeList
) {}
