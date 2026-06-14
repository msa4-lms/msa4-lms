package com.msa4lms.domain.lecture.mapper;

import com.msa4lms.domain.lecture.responses.LectureRes;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface LectureMapper {
    List<LectureRes> findAllLectures();
}
