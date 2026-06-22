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
        return List.of(
            new CollegeWithDepartmentsRes(1L, "C01", "인문대학", List.of(
                new CollegeWithDepartmentsRes.DepartmentDetail(1L, "101", "국어국문학과"),
                new CollegeWithDepartmentsRes.DepartmentDetail(2L, "102", "중국어문화학과"),
                new CollegeWithDepartmentsRes.DepartmentDetail(3L, "103", "영어영문학과")
            )),
            new CollegeWithDepartmentsRes(2L, "C02", "공과대학", List.of(
                new CollegeWithDepartmentsRes.DepartmentDetail(4L, "141", "컴퓨터학부"),
                new CollegeWithDepartmentsRes.DepartmentDetail(5L, "142", "전자공학과"),
                new CollegeWithDepartmentsRes.DepartmentDetail(6L, "150", "기계공학과"),
                new CollegeWithDepartmentsRes.DepartmentDetail(10L, "170", "생명공학과"),
                new CollegeWithDepartmentsRes.DepartmentDetail(14L, "180", "미래자동차공학과"),
                new CollegeWithDepartmentsRes.DepartmentDetail(15L, "190", "로봇공학과")
            )),
            new CollegeWithDepartmentsRes(3L, "C03", "경영대학", List.of(
                new CollegeWithDepartmentsRes.DepartmentDetail(7L, "161", "경영학과"),
                new CollegeWithDepartmentsRes.DepartmentDetail(8L, "162", "경제금융학과"),
                new CollegeWithDepartmentsRes.DepartmentDetail(9L, "163", "회계세무학과")
            )),
            new CollegeWithDepartmentsRes(4L, "C04", "자연과학대학", List.of(
                new CollegeWithDepartmentsRes.DepartmentDetail(12L, "120", "통계학과")
            )),
            new CollegeWithDepartmentsRes(5L, "C05", "사회과학대학", List.of(
                new CollegeWithDepartmentsRes.DepartmentDetail(11L, "110", "심리학과"),
                new CollegeWithDepartmentsRes.DepartmentDetail(13L, "130", "사회학과")
            ))
        );
    }

}
