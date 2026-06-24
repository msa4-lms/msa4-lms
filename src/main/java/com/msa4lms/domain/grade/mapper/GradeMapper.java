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

    void insertGrade(Grade grade);

    void updateGradeScores(Grade grade);

    void updateGradeStatus(@Param("id") Long id, @Param("status") String status);

    void updateObjection(@Param("id") Long id, @Param("objectionReason") String objectionReason);

    void updateObjectionReply(
        @Param("id") Long id, 
        @Param("objectionReply") String objectionReply, 
        @Param("status") String status
    );

    Grade findGradeById(@Param("id") Long id);

    Grade findGradeByEnrollmentId(@Param("enrollmentId") Long enrollmentId);

    List<Grade> findGradesByLectureId(@Param("lectureId") Long lectureId);
}
