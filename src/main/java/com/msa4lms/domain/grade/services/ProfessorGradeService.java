package com.msa4lms.domain.grade.services;

import com.msa4lms.domain.grade.mapper.ProfessorGradeMapper;
import com.msa4lms.domain.grade.requests.GradeSaveDto;
import com.msa4lms.domain.grade.requests.SaveGradesReq;
import com.msa4lms.domain.grade.responses.GradeDetailRes;
import com.msa4lms.domain.grade.responses.ProfessorLectureRes;
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
    public void saveGrades(Long professorId, Long lectureId, SaveGradesReq req) {
        int count = professorGradeMapper.checkLectureOwnership(professorId, lectureId);
        if (count == 0) {
            throw new IllegalArgumentException("해당 강의에 대한 권한이 없습니다.");
        }

        for (GradeSaveDto dto : req.gradeList()) {
            professorGradeMapper.upsertGrade(lectureId, dto);
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
    public void replyObjection(Long professorId, Long gradeId, ReplyObjectionReq req) {
        Long lectureId = professorGradeMapper.findLectureIdByGradeId(gradeId);
        int count = professorGradeMapper.checkLectureOwnership(professorId, lectureId);
        if (count == 0) {
            throw new IllegalArgumentException("해당 강의에 대한 권한이 없습니다.");
        }

        if (req.approve()) {
            GradeSaveDto newScores = req.newScores();
            if (newScores != null) {
                professorGradeMapper.upsertGrade(lectureId, newScores);
            }
            professorGradeMapper.updateGradeStatus(gradeId, "APPROVED", req.reply());
        } else {
            professorGradeMapper.updateGradeStatus(gradeId, "OPENED", req.reply());
        }
    }

    @Transactional
    public List<ProfessorLectureRes> getLecture(Long id) {
        return professorGradeMapper.findLecturesByProfessor(id);
    }
}
