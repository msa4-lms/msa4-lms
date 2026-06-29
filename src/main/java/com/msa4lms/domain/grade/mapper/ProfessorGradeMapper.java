package com.msa4lms.domain.grade.mapper;

import com.msa4lms.domain.grade.requests.GradeCorrectionItemReq;
import com.msa4lms.domain.grade.requests.GradeSaveItemReq;
import com.msa4lms.domain.grade.responses.GradeDetailRes;
import com.msa4lms.domain.grade.responses.ProfessorLectureRes;
import com.msa4lms.domain.lecture.entities.Lecture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfessorGradeMapper {
    int checkLectureOwnership(@Param("professorId") Long professorId, @Param("lectureId") Long lectureId);

    List<GradeDetailRes> findGradesByLectureId(@Param("lectureId") Long lectureId);

    void upsertGrade(@Param("dto") GradeSaveItemReq dto, @Param("totalScore") double totalScore, @Param("letterGrade") String letterGrade);

    Lecture findLectureById(@Param("lectureId") Long lectureId);

    void updateGradesStatusByLectureId(@Param("lectureId") Long lectureId, @Param("status") String status);

    Long findLectureIdByGradeId(@Param("gradeId") Long gradeId);

    List<ProfessorLectureRes> findLecturesByProfessor(@Param("userId") Long professorId);

    String findGradeStatusByLectureId(@Param("lectureId") Long lectureId);

    void correctGrade(@Param("dto") GradeCorrectionItemReq dto);
}
