package com.msa4lms.domain.lecture.services;

import com.msa4lms.domain.lecture.mapper.LectureMapper;
import com.msa4lms.domain.lecture.requests.LectureCreateReq;
import com.msa4lms.domain.lecture.requests.LectureSearchReq;
import com.msa4lms.domain.lecture.responses.LecturePagedRes;
import com.msa4lms.domain.lecture.responses.LectureRes;
import com.msa4lms.domain.lecture.responses.CollegeWithDepartmentsRes;
import com.msa4lms.domain.lecture.responses.FlatCollegeDeptDto;
import java.util.List;
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
        List<com.msa4lms.domain.lecture.responses.FlatCollegeDeptDto> flatDepts = lectureMapper.findFlatCollegesAndDepartments();

        return flatDepts.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        com.msa4lms.domain.lecture.responses.FlatCollegeDeptDto::collegeId,
                        java.util.LinkedHashMap::new,
                        java.util.stream.Collectors.toList()
                ))
                .values().stream()
                .map(list -> {
                    com.msa4lms.domain.lecture.responses.FlatCollegeDeptDto first = list.get(0);
                    List<CollegeWithDepartmentsRes.DepartmentDetail> depts = list.stream()
                            .filter(dto -> dto.deptId() != null)
                            .map(dto -> new CollegeWithDepartmentsRes.DepartmentDetail(
                                    dto.deptId(), dto.deptCode(), dto.deptName()
                            ))
                            .toList();
                    return new CollegeWithDepartmentsRes(
                            first.collegeId(), first.collegeCode(), first.collegeName(), depts
                    );
                })
                .toList();
    }

    private record CollegeKey(Long id, String code, String name) {}
}
