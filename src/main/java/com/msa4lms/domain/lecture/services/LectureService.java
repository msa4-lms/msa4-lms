package com.msa4lms.domain.lecture.services;

import com.msa4lms.domain.lecture.mapper.LectureMapper;
import com.msa4lms.domain.lecture.responses.LectureRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {

    private final LectureMapper lectureMapper;

    public List<LectureRes> getAllLectures() {
        return lectureMapper.findAllLectures();
    }
}
