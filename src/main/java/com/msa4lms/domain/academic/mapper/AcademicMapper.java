package com.msa4lms.domain.academic.mapper;

import com.msa4lms.domain.academic.responses.AcademicAttendanceRes;
import com.msa4lms.domain.academic.responses.SemesterGradeRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AcademicMapper {
    // 학생의 성적 목록 조회 (확정된 것만)
    List<SemesterGradeRes> findGradesByStudentId(@Param("studentId") long studentId);

    // 학생의 출결 현황 조회
    List<AcademicAttendanceRes> findAttendanceByStudentId(@Param("studentId") long studentId);
}
