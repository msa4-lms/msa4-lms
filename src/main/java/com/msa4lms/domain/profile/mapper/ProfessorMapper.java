package com.msa4lms.domain.profile.mapper;

import com.msa4lms.domain.profile.responses.ProfessorProfileRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProfessorMapper {
    ProfessorProfileRes findProfileByUserId(
            @Param("userId") Long userId
    );
}
