package com.msa4lms.domain.professor.responses;

import lombok.Builder;

@Builder
public record ProfessorProfileRes(
        String name
        , String status
        , String departmentName
        , String professorNo
        , String phoneNum
        , String address
        , String email
        , String collegeName
        , Integer hireYear

) {
}
