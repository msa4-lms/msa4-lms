package com.msa4lms.domain.lecture.responses;

import java.util.List;

public record CollegeWithDepartmentsRes(
    Long id,
    String code,
    String name,
    List<DepartmentDetail> departments
) {
    public record DepartmentDetail(
        Long id,
        String code,
        String name
    ) {}
}
