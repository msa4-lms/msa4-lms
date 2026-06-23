package com.msa4lms.domain.lecture.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private Long id;
    private String code;
    private String name;
    private Integer credits;
    private Long departmentId;
    private Integer targetGrade;
    private String completionType;
}
