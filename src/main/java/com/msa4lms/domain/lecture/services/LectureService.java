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

    @Transactional
    public void createLecture(Long professorId, LectureCreateReq req) {
        if (req.midtermRatio() + req.finalRatio() + req.assignmentRatio() + req.attendanceRatio() != 100) {
            throw new IllegalArgumentException("성적 비율의 합은 100이어야 합니다.");
        }

        com.msa4lms.domain.lecture.entities.Lecture lecture = new com.msa4lms.domain.lecture.entities.Lecture();
        lecture.setSemesterId(req.semesterId());
        lecture.setCourseId(req.courseId());
        lecture.setProfessorId(professorId);
        lecture.setSectionNo(req.sectionNo());
        lecture.setCapacity(req.capacity());
        lecture.setClassroom(req.classroom());
        lecture.setMidtermRatio(req.midtermRatio());
        lecture.setFinalRatio(req.finalRatio());
        lecture.setAssignmentRatio(req.assignmentRatio());
        lecture.setAttendanceRatio(req.attendanceRatio());

        lectureMapper.insertLecture(lecture);

        // 시간표 저장
        if (req.schedules() != null) {
            for (com.msa4lms.domain.lecture.requests.ScheduleInput schedule : req.schedules()) {
                if (schedule.startPeriod() > schedule.endPeriod()) {
                    throw new IllegalArgumentException("시작 교시는 종료 교시보다 클 수 없습니다.");
                }
                lectureMapper.insertLectureSchedule(lecture.getId(), schedule);
            }
        }
    }

    public List<LectureRes> getLecturesByProfessor(Long professorId) {
        return lectureMapper.findLecturesByProfessor(professorId);
    }

    public List<CollegeWithDepartmentsRes> getCollegesWithDepartments() {
        List<FlatCollegeDeptDto> flatDepts = lectureMapper.findFlatCollegesAndDepartments();

        return flatDepts.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        FlatCollegeDeptDto::collegeId,
                        java.util.LinkedHashMap::new,
                        java.util.stream.Collectors.toList()
                ))
                .values().stream()
                .map(list -> {
                    FlatCollegeDeptDto first = list.get(0);
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

    public List<LectureRes> getLecturesByProfessorId(Long professorId) {
        return lectureMapper.findLecturesByProfessorId(professorId);
    }
}
