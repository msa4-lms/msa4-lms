package com.msa4lms.domain.user.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Integer id;
    private String loginId;
    private String name;
    private String email;
    private String password;
    private Role role;
    private Integer departmentId;
    private String refreshToken;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
}
