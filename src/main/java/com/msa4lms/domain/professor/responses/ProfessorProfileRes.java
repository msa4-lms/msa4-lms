package com.msa4lms.domain.professor.responses;

import lombok.Builder;

@Builder
public record ProfessorProfileRes(
        String name
        , String status
        , String departmentName
        , String professorNo
        , String email
        , Integer hireYear
        // 나머지는 db 추가 후 넣기
) {
}
