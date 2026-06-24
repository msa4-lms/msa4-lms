package com.msa4lms.domain.grade.mapper;

import com.msa4lms.domain.grade.requests.GradeSaveDto;
import com.msa4lms.domain.grade.responses.GradeDetailRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfessorGradeMapper {
    int checkLectureOwnership(@Param("professorId") Long professorId, @Param("lectureId") Long lectureId);

    List<GradeDetailRes> findGradesByLectureId(@Param("lectureId") Long lectureId);

    void upsertGrade(@Param("lectureId") Long lectureId, @Param("dto") GradeSaveDto dto);

    void updateGradesStatusByLectureId(@Param("lectureId") Long lectureId, @Param("status") String status);

    Long findLectureIdByGradeId(@Param("gradeId") Long gradeId);

    void updateGradeStatus(@Param("gradeId") Long gradeId, @Param("status") String status, @Param("objectionReply") String objectionReply);
}
