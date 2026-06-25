package com.msa4lms.domain.department.entities;

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
public class Department {
    private Long id;
    private String code;
    private String name;
    private Integer active;
}

