package com.msa4lms.domain.student.responses;

import lombok.Builder;

@Builder
public record StudentProfileRes(
        String name
        ,String status
        ,String departmentName
        ,Integer grade
        ,String studentNo
        ,String email
        ,String phoneNum
        ,String address
        ,String collegeName
        ,String advisorName
        ,Integer admissionYear
        ,Integer credits
) {

}
