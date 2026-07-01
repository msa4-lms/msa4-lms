package com.msa4lms.domain.lecture.responses;

public record CollegeDeptRes(
        Long collegeId,
        String collegeCode,
        String collegeName,
        Long deptId,
        String deptCode,
        String deptName
) {}
