package com.msa4lms.domain.grade.mapper;

import com.msa4lms.domain.grade.entities.Grade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface GradeMapper {
    List<com.msa4lms.domain.grade.responses.SemesterGradeRes> findGradesByStudentId(
            @Param("studentId") long studentId,
            @Param("year") Integer year,
            @Param("semester") Integer semester);

}
