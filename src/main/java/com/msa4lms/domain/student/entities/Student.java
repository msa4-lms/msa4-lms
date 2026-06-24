package com.msa4lms.domain.student.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {
    private Long id;
    private Long userId;
    private Long departmentId;
    private Long majorId;
    private Integer gradeLevel;
    private Integer admissionYear;
    private String academicStatus;
    private Long advisorId;
}
