package com.msa4lms.domain.grade.services;

import com.msa4lms.domain.grade.mapper.GradeMapper;
import com.msa4lms.domain.grade.responses.GradeSummaryRes;
import com.msa4lms.domain.grade.responses.SemesterGradeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradeService {

    private final GradeMapper gradeMapper;

    /**
     * 학생의 전체 성적 요약 및 평점 계산
     */
    public GradeSummaryRes getGradeSummary(long studentId, Integer year, Integer semester) {
        List<SemesterGradeRes> grades = gradeMapper.findGradesByStudentId(studentId, year, semester);

        if (grades == null || grades.isEmpty()) {
            return GradeSummaryRes.builder()
                    .totalGpa(0.0)
                    .totalCredits(0)
                    .semesterGrades(List.of())
                    .build();
        }

        double totalGradePointSum = 0;
        int totalCredits = 0;

        for (SemesterGradeRes g : grades) {
            if (g == null || g.gradePoint() == null || g.credits() == null) continue;
            totalGradePointSum += (g.gradePoint() * g.credits());
            totalCredits += g.credits();
        }

        double totalGpa = totalCredits == 0 ? 0.0 : Math.round((totalGradePointSum / totalCredits) * 100.0) / 100.0;

        return GradeSummaryRes.builder()
                .totalGpa(totalGpa)
                .totalCredits(totalCredits)
                .semesterGrades(grades)
                .build();
    }


}
