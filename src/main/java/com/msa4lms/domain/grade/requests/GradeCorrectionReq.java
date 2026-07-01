package com.msa4lms.domain.grade.requests;

import java.util.List;

public record GradeCorrectionReq(
        List<GradeCorrectionItemReq> correctionList
) {}
