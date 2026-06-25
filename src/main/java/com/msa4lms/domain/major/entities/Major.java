package com.msa4lms.domain.major.entities;

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
public class Major {
    private Long id;
    private Long departmentId;
    private String code;
    private String name;
    private Integer active;
}

