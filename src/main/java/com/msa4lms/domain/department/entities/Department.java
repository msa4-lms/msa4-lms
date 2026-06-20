package com.msa4lms.domain.department.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Department {
    private Long id;
    private String code;
    private String name;
    private Integer active;
}
