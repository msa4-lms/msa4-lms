package com.msa4lms.domain.professor.mapper;

import com.msa4lms.domain.professor.responses.ProfessorProfileRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProfessorMapper {
    ProfessorProfileRes findProfileByUserId(
            @Param("userId") Long userId
    );
}
