package com.msa4lms.domain.grade.services;

import com.msa4lms.domain.grade.entities.Grade;
import com.msa4lms.domain.grade.entities.GradeStatus;
import com.msa4lms.domain.grade.mapper.GradeMapper;
import com.msa4lms.domain.grade.requests.GradeBatchInputReq;
import com.msa4lms.domain.grade.requests.GradeInputReq;
import com.msa4lms.domain.lecture.entities.Lecture;
import com.msa4lms.domain.lecture.mapper.LectureMapper;
import com.msa4lms.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradeService {

    private final GradeMapper gradeMapper;
    private final LectureMapper lectureMapper;
    private final UserMapper userMapper;

    /**
     * 학생의 전체 성적 요약 및 평점 계산
     */
    public com.msa4lms.domain.grade.responses.GradeSummaryRes getGradeSummary(long studentId, Integer year, Integer semester) {
        List<com.msa4lms.domain.grade.responses.SemesterGradeRes> grades = gradeMapper.findGradesByStudentId(studentId, year, semester);

        if (grades == null || grades.isEmpty()) {
            return com.msa4lms.domain.grade.responses.GradeSummaryRes.builder()
                    .totalGpa(0.0)
                    .totalCredits(0)
                    .semesterGrades(List.of())
                    .build();
        }

        double totalGradePointSum = 0;
        int totalCredits = 0;

        for (com.msa4lms.domain.grade.responses.SemesterGradeRes g : grades) {
            if (g == null || g.getGradePoint() == null || g.getCredits() == null) continue;
            totalGradePointSum += (g.getGradePoint() * g.getCredits());
            totalCredits += g.getCredits();
        }

        double totalGpa = totalCredits == 0 ? 0.0 : Math.round((totalGradePointSum / totalCredits) * 100.0) / 100.0;

        return com.msa4lms.domain.grade.responses.GradeSummaryRes.builder()
                .totalGpa(totalGpa)
                .totalCredits(totalCredits)
                .semesterGrades(grades)
                .build();
    }

}
