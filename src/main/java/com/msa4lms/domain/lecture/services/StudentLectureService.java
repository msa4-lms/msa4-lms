package com.msa4lms.domain.lecture.services;

import com.msa4lms.domain.lecture.mapper.StudentLectureMapper;
import com.msa4lms.domain.lecture.requests.LectureCreateReq;
import com.msa4lms.domain.lecture.requests.LectureSearchReq;
import com.msa4lms.domain.lecture.responses.LecturePagedRes;
import com.msa4lms.domain.lecture.responses.LectureRes;
import com.msa4lms.domain.lecture.entities.Course;
import com.msa4lms.domain.lecture.entities.Lecture;
import com.msa4lms.domain.lecture.requests.ScheduleInput;
import com.msa4lms.domain.lecture.responses.CollegeWithDepartmentsRes;
import com.msa4lms.domain.lecture.responses.CourseRes;
import com.msa4lms.domain.lecture.dto.CollegeDeptDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentLectureService {

    private final StudentLectureMapper lectureMapper;

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

        Long courseId = req.courseId();

        if (Boolean.TRUE.equals(req.isNewCourse())) {
            Long departmentId = lectureMapper.findDepartmentIdByProfessorId(professorId);
            String newCode = String.format("%05d", ThreadLocalRandom.current().nextInt(10000, 100000)); // 10000~99999 랜덤 5자리
            
            Course newCourse = Course.builder()
                .code(newCode)
                .name(req.newCourseName())
                .credits(req.newCourseCredits())
                .departmentId(departmentId)
                .targetGrade(req.newCourseTargetGrade())
                .completionType(req.newCourseCompletionType() != null ? req.newCourseCompletionType() : "GENERAL_ELECTIVE")
                .build();

            lectureMapper.insertCourse(newCourse);
            courseId = newCourse.getId();
        }

        Lecture lecture = Lecture.builder()
                .semesterId(req.semesterId())
                .courseId(courseId)
                .professorId(professorId)
                .sectionNo(req.sectionNo())
                .capacity(req.capacity())
                .classroom(req.classroom())
                .midtermRatio(req.midtermRatio())
                .finalRatio(req.finalRatio())
                .assignmentRatio(req.assignmentRatio())
                .attendanceRatio(req.attendanceRatio())
                .syllabus(req.syllabus())
                .build();

        lectureMapper.insertLecture(lecture);

        // 시간표 저장
        if (req.schedules() != null) {
            for (ScheduleInput schedule : req.schedules()) {
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
        List<CollegeDeptDto> flatDepts = lectureMapper.findFlatCollegesAndDepartments();

        return flatDepts.stream()
                .collect(Collectors.groupingBy(
                        CollegeDeptDto::collegeId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .values().stream()
                .map(list -> {
                    CollegeDeptDto first = list.get(0);
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

    public List<LectureRes> getLecturesByProfessorId(Long professorId, Integer year, Integer semester) {
        return lectureMapper.findLecturesByProfessorId(professorId, year, semester);
    }

    public List<LectureRes> getPastLecturesByProfessorId(Long professorId) {
        return lectureMapper.findPastLecturesByProfessorId(professorId);
    }

    public List<CourseRes> getAvailableCoursesForProfessor(Long professorId) {
        return lectureMapper.findAvailableCoursesForProfessor(professorId);
    }
}
