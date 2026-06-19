package com.msa4lms.domain.lecture.services;

import com.msa4lms.domain.lecture.mapper.LectureMapper;
import com.msa4lms.domain.lecture.requests.LectureSearchReq;
import com.msa4lms.domain.lecture.responses.LecturePagedRes;
import com.msa4lms.domain.lecture.responses.LectureRes;
import com.msa4lms.domain.lecture.responses.CollegeWithDepartmentsRes;
import com.msa4lms.domain.lecture.responses.FlatCollegeDeptDto;
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

    public LecturePagedRes searchLectures(LectureSearchReq searchReq) {
        int offset = (searchReq.page() - 1) * searchReq.size();
        List<LectureRes> lectures = lectureMapper.findLecturesBySearch(searchReq, offset, searchReq.size());
        long totalCount = lectureMapper.countLecturesBySearch(searchReq);
        return new LecturePagedRes(lectures, totalCount, searchReq.page(), searchReq.size());
    }

    public List<CollegeWithDepartmentsRes> getCollegesWithDepartments() {
        List<FlatCollegeDeptDto> flatList = lectureMapper.findFlatCollegesAndDepartments();

        return flatList.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                dto -> new CollegeKey(dto.collegeId(), dto.collegeCode(), dto.collegeName()),
                java.util.LinkedHashMap::new,
                java.util.stream.Collectors.mapping(
                    dto -> new CollegeWithDepartmentsRes.DepartmentDetail(dto.deptId(), dto.deptCode(), dto.deptName()),
                    java.util.stream.Collectors.toList()
                )
            ))
            .entrySet().stream()
            .map(entry -> new CollegeWithDepartmentsRes(
                entry.getKey().id(),
                entry.getKey().code(),
                entry.getKey().name(),
                entry.getValue().stream()
                    .filter(d -> d.id() != null)
                    .toList()
            ))
            .toList();
    }

    private record CollegeKey(Long id, String code, String name) {}
}
