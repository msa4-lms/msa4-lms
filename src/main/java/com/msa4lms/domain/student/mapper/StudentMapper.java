package com.msa4lms.domain.student.mapper;

import com.msa4lms.domain.student.responses.StudentProfileRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StudentMapper {

    StudentProfileRes findProfileByUserId(
            @Param("userId") Long userId
    );
}
