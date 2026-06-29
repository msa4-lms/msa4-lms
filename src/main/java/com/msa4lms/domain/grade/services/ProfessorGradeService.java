package com.msa4lms.domain.grade.services;

import com.msa4lms.domain.grade.mapper.ProfessorGradeMapper;
import com.msa4lms.domain.grade.requests.GradeSaveDto;
import com.msa4lms.domain.grade.requests.GradeSaveReq;
import com.msa4lms.domain.grade.responses.GradeDetailRes;
import com.msa4lms.domain.grade.responses.ProfessorLectureRes;
import com.msa4lms.domain.lecture.entities.Lecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfessorGradeService {

    private final ProfessorGradeMapper professorGradeMapper;

    public List<GradeDetailRes> getGradesForLecture(Long professorId, Long lectureId) {
        // 보안 검증: 이 강의가 이 교수의 강의인지 확인 필요.
        int count = professorGradeMapper.checkLectureOwnership(professorId, lectureId);
        if (count == 0) {
            throw new IllegalArgumentException("해당 강의에 대한 권한이 없습니다.");
        }
        return professorGradeMapper.findGradesByLectureId(lectureId);
    }

    @Transactional
    public void saveGrades(Long professorId, Long lectureId, GradeSaveReq req) {
        int count = professorGradeMapper.checkLectureOwnership(professorId, lectureId);
        if (count == 0) {
            throw new IllegalArgumentException("해당 강의에 대한 권한이 없습니다.");
        }

        for (GradeSaveDto dto : req.gradeList()) {
            calculateAndUpsertGrade(lectureId, dto);
        }
    }

    @Transactional
    public void updateGradesStatus(Long professorId, Long lectureId, String status) {
        int count = professorGradeMapper.checkLectureOwnership(professorId, lectureId);
        if (count == 0) {
            throw new IllegalArgumentException("해당 강의에 대한 권한이 없습니다.");
        }

        professorGradeMapper.updateGradesStatusByLectureId(lectureId, status);
    }


    @Transactional
    public List<ProfessorLectureRes> getLecture(Long id) {
        return professorGradeMapper.findLecturesByProfessor(id);
    }

    private void calculateAndUpsertGrade(Long lectureId, GradeSaveDto dto) {
        Lecture lecture = professorGradeMapper.findLectureById(lectureId);
        if (lecture == null) {
            throw new IllegalArgumentException("해당 강의를 찾을 수 없습니다.");
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
        String letterGrade;
        if (dto.grade() != null && !dto.grade().isBlank()) {
            letterGrade = dto.grade();
        } else {
            letterGrade = "F";
            if (totalScore >= 95) letterGrade = "A+";
            else if (totalScore >= 90) letterGrade = "A";
            else if (totalScore >= 85) letterGrade = "B+";
            else if (totalScore >= 80) letterGrade = "B";
            else if (totalScore >= 75) letterGrade = "C+";
            else if (totalScore >= 70) letterGrade = "C";
            else if (totalScore >= 65) letterGrade = "D+";
            else if (totalScore >= 60) letterGrade = "D";
        }

        professorGradeMapper.upsertGrade(dto, totalScore, letterGrade);
    }
}
