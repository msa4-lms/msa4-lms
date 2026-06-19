package com.msa4lms.domain.user.responses;

import com.msa4lms.domain.user.entities.Role;
import lombok.Builder;

@Builder
public record UserRes(
        Long id
        , String loginId
        , String name
        , String email
        , Role role
        , Long departmentId
        , String departmentName
        , String createdAt
) {
}
