package com.msa4lms.domain.user.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id;
    private String loginId;
    private String name;
    private String email;
    private String password;
    private Role role;
    private Long departmentId;
    private String departmentName;
    private String refreshToken;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
}
