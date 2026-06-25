package com.msa4lms.domain.user.entities;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String loginId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String password;
    private Role role;
    private Long departmentId;
    private String departmentName;
    private Integer gradeLevel;
    private Long advisorId;
    private String advisorName;
    private String refreshToken;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
}

