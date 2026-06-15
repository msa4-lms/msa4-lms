package com.msa4lms.domain.lecture.mapper;

import com.msa4lms.domain.lecture.requests.LectureSearchReq;
import com.msa4lms.domain.lecture.responses.LectureRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface LectureMapper {
    List<LectureRes> findAllLectures();
    
    List<LectureRes> findLecturesBySearch(
        @Param("searchReq") LectureSearchReq searchReq, 
        @Param("offset") int offset, 
        @Param("size") int size
    );
    
    long countLecturesBySearch(@Param("searchReq") LectureSearchReq searchReq);
}
