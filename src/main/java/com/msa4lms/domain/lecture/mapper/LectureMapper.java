package com.msa4lms.domain.lecture.mapper;

import com.msa4lms.domain.lecture.requests.LectureSearchReq;
import com.msa4lms.domain.lecture.requests.ScheduleInput;
import com.msa4lms.domain.lecture.responses.LectureRes;
import com.msa4lms.domain.lecture.responses.FlatCollegeDeptDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.msa4lms.domain.lecture.entities.Lecture;

@Mapper
public interface LectureMapper {
    List<LectureRes> findAllLectures();
    
    List<LectureRes> findLecturesBySearch(
        @Param("searchReq") LectureSearchReq searchReq, 
        @Param("offset") int offset, 
        @Param("size") int size
    );
    
    long countLecturesBySearch(@Param("searchReq") LectureSearchReq searchReq);

    void insertLecture(Lecture lecture);

    List<LectureRes> findLecturesByProfessor(@Param("professorId") Long professorId);

    List<FlatCollegeDeptDto> findFlatCollegesAndDepartments();

    List<LectureRes> findLecturesByProfessorId(@Param("professorId") Long professorId);

    Lecture findLectureById(@Param("id") Long id);

    void insertLectureSchedule(@Param("lectureId") Long lectureId, @Param("schedule") ScheduleInput schedule);
}
