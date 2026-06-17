package com.msa4lms.domain.academic.responses;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterGradeRes {
    private Integer year;
    private Integer semester;
    private String courseName;
    private String courseCode;
    private Integer credits;
    private String grade;
    private Double gradePoint;
}
