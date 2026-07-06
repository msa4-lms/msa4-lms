package com.msa4lms.domain.grade.services;

import com.msa4lms.domain.grade.mapper.ProfessorGradeMapper;
import com.msa4lms.domain.grade.requests.GradeCorrectionItemReq;
import com.msa4lms.domain.grade.requests.GradeSaveItemReq;
import com.msa4lms.domain.grade.requests.GradeCorrectionReq;
import com.msa4lms.domain.grade.requests.GradeSaveReq;
import com.msa4lms.domain.grade.responses.GradeDetailRes;
import com.msa4lms.domain.grade.responses.ProfessorLectureRes;
import com.msa4lms.domain.lecture.entities.Lecture;
import com.msa4lms.global.errors.custom.ForbiddenException;
import com.msa4lms.global.errors.custom.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfessorGradeService {

    private final ProfessorGradeMapper professorGradeMapper;

    public List<GradeDetailRes> getGradesForLecture(Long professorId, Long lectureId) {
        // 보안 검증: 이 강의가 이 교수의 강의인지 확인 필요.
        int count = professorGradeMapper.checkLectureOwnership(professorId, lectureId);
        if (count == 0) {
            throw new ForbiddenException("해당 강의에 대한 권한이 없습니다.");
        }
        return professorGradeMapper.findGradesByLectureId(lectureId);
    }

    @Transactional
    public void saveGrades(Long professorId, Long lectureId, GradeSaveReq req) {
        int count = professorGradeMapper.checkLectureOwnership(professorId, lectureId);
        if (count == 0) {
            throw new ForbiddenException("해당 강의에 대한 권한이 없습니다.");
        }

        String currentStatus = professorGradeMapper.findGradeStatusByLectureId(lectureId);
        if (currentStatus != null && !"DRAFT".equals(currentStatus)) {
            throw new IllegalStateException("이미 학생에게 공개된 성적입니다. 성적 정정 절차를 이용해주세요.");
        }

        for (GradeSaveItemReq dto : req.gradeList()) {
            calculateAndUpsertGrade(lectureId, dto);
        }
    }

    @Transactional
    public void correctGrades(Long professorId, Long lectureId, GradeCorrectionReq req) {
        int count = professorGradeMapper.checkLectureOwnership(professorId, lectureId);
        if (count == 0) {
            throw new ForbiddenException("해당 강의에 대한 권한이 없습니다.");
        }

        String currentStatus = professorGradeMapper.findGradeStatusByLectureId(lectureId);
        if (currentStatus == null || "DRAFT".equals(currentStatus)) {
            throw new IllegalStateException("아직 제출되지 않은 성적입니다. 성적 입력을 이용해주세요.");
        }
        if ("FINAL".equals(currentStatus)) {
            throw new IllegalStateException("최종 확정된 성적은 정정할 수 없습니다.");
        }

        for (GradeCorrectionItemReq dto : req.correctionList()) {
            calculateAndCorrectGrade(lectureId, dto);
        }
    }

    private void calculateAndCorrectGrade(Long lectureId, GradeCorrectionItemReq dto) {
        Lecture lecture = professorGradeMapper.findLectureById(lectureId);
        if (lecture == null) {
            throw new RecordNotFoundException("해당 강의를 찾을 수 없습니다.");
        }

        int midtermRatio = lecture.getMidtermRatio() != null ? lecture.getMidtermRatio() : 30;
        int finalRatio = lecture.getFinalRatio() != null ? lecture.getFinalRatio() : 30;
        int assignmentRatio = lecture.getAssignmentRatio() != null ? lecture.getAssignmentRatio() : 30;
        int attendanceRatio = lecture.getAttendanceRatio() != null ? lecture.getAttendanceRatio() : 10;

        double midterm = dto.midtermScore() != null ? dto.midtermScore() : 0.0;
        double finalS = dto.finalScore() != null ? dto.finalScore() : 0.0;
        double assignment = dto.assignmentScore() != null ? dto.assignmentScore() : 0.0;
        double attendance = dto.attendanceScore() != null ? dto.attendanceScore() : 0.0;

        double totalScore = (midterm * midtermRatio / 100.0) +
                            (finalS * finalRatio / 100.0) +
                            (assignment * assignmentRatio / 100.0) +
                            (attendance * attendanceRatio / 100.0);

        String letterGrade = (dto.grade() != null && !dto.grade().isBlank())
                ? dto.grade()
                : resolveLetterGrade(totalScore);

        professorGradeMapper.correctGrade(dto, totalScore, letterGrade);
    }

    @Transactional
    public void updateGradesStatus(Long professorId, Long lectureId, String status) {
        int count = professorGradeMapper.checkLectureOwnership(professorId, lectureId);
        if (count == 0) {
            throw new ForbiddenException("해당 강의에 대한 권한이 없습니다.");
        }

        // 허용된 상태값만 사용 가능 (OPENED: 임시저장 공개, FINAL: 최종 제출 확정)
        Set<String> allowedStatuses = Set.of("OPENED", "FINAL");
        if (!allowedStatuses.contains(status)) {
            throw new IllegalArgumentException("허용되지 않은 성적 상태값입니다. (허용: OPENED, FINAL)");
        }

        professorGradeMapper.updateGradesStatusByLectureId(lectureId, status);
    }


    @Transactional
    public List<ProfessorLectureRes> getLecture(Long id) {
        return professorGradeMapper.findLecturesByProfessor(id);
    }

    private void calculateAndUpsertGrade(Long lectureId, GradeSaveItemReq dto) {
        Lecture lecture = professorGradeMapper.findLectureById(lectureId);
        if (lecture == null) {
            throw new RecordNotFoundException("해당 강의를 찾을 수 없습니다.");
        }

        int midtermRatio = lecture.getMidtermRatio() != null ? lecture.getMidtermRatio() : 30;
        int finalRatio = lecture.getFinalRatio() != null ? lecture.getFinalRatio() : 30;
        int assignmentRatio = lecture.getAssignmentRatio() != null ? lecture.getAssignmentRatio() : 30;
        int attendanceRatio = lecture.getAttendanceRatio() != null ? lecture.getAttendanceRatio() : 10;

        double midterm = dto.midtermScore() != null ? dto.midtermScore() : 0.0;
        double finalS = dto.finalScore() != null ? dto.finalScore() : 0.0;
        double assignment = dto.assignmentScore() != null ? dto.assignmentScore() : 0.0;
        double attendance = dto.attendanceScore() != null ? dto.attendanceScore() : 0.0;

        double totalScore = (midterm * midtermRatio / 100.0) +
                            (finalS * finalRatio / 100.0) +
                            (assignment * assignmentRatio / 100.0) +
                            (attendance * attendanceRatio / 100.0);

        // 프론트엔드에서 명시적으로 등급을 전달한 경우 해당 값을 우선 사용 (성적 정정 시 + 부여 등)
        String letterGrade = (dto.grade() != null && !dto.grade().isBlank())
                ? dto.grade()
                : resolveLetterGrade(totalScore);

        professorGradeMapper.upsertGrade(dto, totalScore, letterGrade);
    }

    // 총점(0~100, double)을 문자 등급으로 변환. 임계값도 double로 명시해 비교 타입을 일치시킨다.
    private String resolveLetterGrade(double totalScore) {
        if (totalScore >= 95.0) return "A+";
        if (totalScore >= 90.0) return "A";
        if (totalScore >= 85.0) return "B+";
        if (totalScore >= 80.0) return "B";
        if (totalScore >= 75.0) return "C+";
        if (totalScore >= 70.0) return "C";
        if (totalScore >= 65.0) return "D+";
        if (totalScore >= 60.0) return "D";
        return "F";
    }
}
