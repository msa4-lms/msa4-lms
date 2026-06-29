package com.msa4lms.domain.lecture.mapper;

import com.msa4lms.domain.lecture.entities.Course;
import com.msa4lms.domain.lecture.entities.Lecture;
import com.msa4lms.domain.lecture.requests.ScheduleInput;
import com.msa4lms.domain.lecture.responses.CourseRes;
import com.msa4lms.domain.lecture.responses.LectureRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfessorLectureMapper {
    Long findDepartmentIdByProfessorId(@Param("professorId") Long professorId);
    void insertCourse(Course course);
    void insertLecture(Lecture lecture);
    void insertLectureSchedule(@Param("lectureId") Long lectureId, @Param("schedule") ScheduleInput schedule);
    List<LectureRes> findLecturesByProfessor(@Param("professorId") Long professorId);
    List<LectureRes> findLecturesByProfessorId(@Param("professorId") Long professorId,
                                               @Param("year") Integer year,
                                               @Param("semester") Integer semester);
    List<LectureRes> findPastLecturesByProfessorId(@Param("professorId") Long professorId);
    List<CourseRes> findAvailableCoursesForProfessor(@Param("professorId") Long professorId);
}
