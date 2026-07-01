package com.msa4lms.domain.grade.responses;

import lombok.*;
import java.util.List;

@Builder
public record GradeSummaryRes(
    Double totalGpa,
    Integer totalCredits,
    List<SemesterGradeRes> semesterGrades
) {}
