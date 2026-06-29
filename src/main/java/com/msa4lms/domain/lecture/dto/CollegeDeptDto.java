package com.msa4lms.domain.lecture.dto;

public record CollegeDeptDto(
        Long collegeId,
        String collegeCode,
        String collegeName,
        Long deptId,
        String deptCode,
        String deptName
) {}
