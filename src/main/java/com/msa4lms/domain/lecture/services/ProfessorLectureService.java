package com.msa4lms.domain.lecture.services;

import com.msa4lms.domain.lecture.mapper.ProfessorLectureMapper;
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

    private final ProfessorLectureMapper lectureMapper;

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
        Long currentSemesterId = lectureMapper.findCurrentSemesterId();
        if (currentSemesterId == null) {
            throw new IllegalStateException("현재 학기 정보가 설정되지 않았습니다.");
        }
        lecture.setSemesterId(currentSemesterId);
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

        // 분반 중복 사전 검증: 같은 학기 + 동일 과목 + 동일 분반은 개설 불가 (uk_lecture_section)
        if (lectureMapper.existsLectureSection(currentSemesterId, courseId, req.sectionNo())) {
            throw new com.msa4lms.global.errors.custom.DuplicatedRecordException(
                String.format("이미 같은 학기에 동일 과목의 %s 분반이 개설되어 있습니다. 다른 분반 번호를 선택해주세요.", req.sectionNo()));
        }

        // 시간표 사전 검증 (INSERT 전): 교시 유효성 + 본인 기존 강의와의 시간 충돌
        if (req.schedules() != null) {
            for (com.msa4lms.domain.lecture.requests.ScheduleInput schedule : req.schedules()) {
                if (schedule.startPeriod() > schedule.endPeriod()) {
                    throw new IllegalArgumentException("시작 교시는 종료 교시보다 클 수 없습니다.");
                }
                if (lectureMapper.hasProfessorScheduleConflict(
                        professorId, currentSemesterId,
                        schedule.dayOfWeek(), schedule.startPeriod(), schedule.endPeriod())) {
                    throw new com.msa4lms.global.errors.custom.LectureTimeConflictException(
                        String.format("이미 같은 학기 %s %d~%d교시에 담당 중인 강의가 있어 시간표가 겹칩니다.",
                            toKoreanDay(schedule.dayOfWeek()), schedule.startPeriod(), schedule.endPeriod()));
                }
            }
        }

        lectureMapper.insertLecture(lecture);

        // 시간표 저장
        if (req.schedules() != null) {
            for (com.msa4lms.domain.lecture.requests.ScheduleInput schedule : req.schedules()) {
                lectureMapper.insertLectureSchedule(lecture.getId(), schedule);
            }
        }
    }

    private String toKoreanDay(String dayOfWeek) {
        if (dayOfWeek == null) return "";
        return switch (dayOfWeek) {
            case "MON" -> "월요일";
            case "TUE" -> "화요일";
            case "WED" -> "수요일";
            case "THU" -> "목요일";
            case "FRI" -> "금요일";
            default -> dayOfWeek;
        };
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
