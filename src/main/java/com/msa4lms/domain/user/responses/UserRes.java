package com.msa4lms.domain.user.responses;

import com.msa4lms.domain.user.entities.Role;
import lombok.Builder;

@Builder
public record UserRes(
        Long id
        , String loginId
        , String name
        , String email
        , String phoneNumber
        , String address
        , Role role
        , Long departmentId
        , String departmentName
        , Integer gradeLevel
        , Long advisorId
        , String advisorName
        , String createdAt
) {
}
