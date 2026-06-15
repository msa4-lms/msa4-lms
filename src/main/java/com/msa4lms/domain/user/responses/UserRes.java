package com.msa4lms.domain.user.responses;

import com.msa4lms.domain.user.entities.Role;
import lombok.Builder;

@Builder
public record UserRes(
        Integer id
        , String userNo
        , String name
        , String email
        , Role role
        , Integer departmentId
        , String createdAt
) {
}
