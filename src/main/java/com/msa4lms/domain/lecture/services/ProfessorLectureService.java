package com.msa4lms.domain.lecture.services;

import com.msa4lms.domain.lecture.mapper.StudentLectureMapper;
import com.msa4lms.domain.lecture.requests.LectureCreateReq;
import com.msa4lms.domain.lecture.responses.LectureRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfessorLectureService {

    private final StudentLectureMapper lectureMapper;

    @Transactional
    public void createLecture(Long professorId, LectureCreateReq req) {
        if (req.midtermRatio() + req.finalRatio() + req.assignmentRatio() + req.attendanceRatio() != 100) {
            throw new IllegalArgumentException("성적 비율의 합은 100이어야 합니다.");
        }

        Long courseId = req.courseId();
        
        if (Boolean.TRUE.equals(req.isNewCourse())) {
            Long departmentId = lectureMapper.findDepartmentIdByProfessorId(professorId);
            String newCode = String.format("%05d", new java.util.Random().nextInt(90000) + 10000); // 10000~99999 랜덤 5자리
            
            com.msa4lms.domain.lecture.entities.Course newCourse = com.msa4lms.domain.lecture.entities.Course.builder()
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

        com.msa4lms.domain.lecture.entities.Lecture lecture = new com.msa4lms.domain.lecture.entities.Lecture();
        lecture.setSemesterId(req.semesterId());
        lecture.setCourseId(courseId);
        lecture.setProfessorId(professorId);
        lecture.setSectionNo(req.sectionNo());
        lecture.setCapacity(req.capacity());
        lecture.setClassroom(req.classroom());
        lecture.setMidtermRatio(req.midtermRatio());
        lecture.setFinalRatio(req.finalRatio());
        lecture.setAssignmentRatio(req.assignmentRatio());
        lecture.setAttendanceRatio(req.attendanceRatio());
        lecture.setSyllabus(req.syllabus());

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

    public List<LectureRes> getLecturesByProfessorId(Long professorId, Integer year, Integer semester) {
        return lectureMapper.findLecturesByProfessorId(professorId, year, semester);
    }

    public List<LectureRes> getPastLecturesByProfessorId(Long professorId) {
        return lectureMapper.findPastLecturesByProfessorId(professorId);
    }

    public List<com.msa4lms.domain.lecture.responses.CourseRes> getAvailableCoursesForProfessor(Long professorId) {
        return lectureMapper.findAvailableCoursesForProfessor(professorId);
    }
}
