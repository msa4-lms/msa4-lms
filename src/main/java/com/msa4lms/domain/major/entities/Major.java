package com.msa4lms.domain.major.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Major {
    private Long id;
    private Long departmentId;
    private String code;
    private String name;
    private Integer active;
}
