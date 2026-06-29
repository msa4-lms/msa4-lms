package com.msa4lms.domain.profile.entities;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

