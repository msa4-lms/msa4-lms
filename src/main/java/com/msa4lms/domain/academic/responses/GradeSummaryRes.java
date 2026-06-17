package com.msa4lms.domain.academic.responses;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeSummaryRes {
    private Double totalGpa;
    private Integer totalCredits;
    private List<SemesterGradeRes> semesterGrades;
}
