package com.msa4lms.domain.academic.services;

import com.msa4lms.domain.academic.mapper.AcademicMapper;
import com.msa4lms.domain.academic.responses.AcademicAttendanceRes;
import com.msa4lms.domain.academic.responses.GradeSummaryRes;
import com.msa4lms.domain.academic.responses.SemesterGradeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicService {
    private final AcademicMapper academicMapper;

    /**
     * 학생의 전체 성적 요약 및 평점 계산
     */
    public GradeSummaryRes getGradeSummary(long studentId) {
        List<SemesterGradeRes> grades = academicMapper.findGradesByStudentId(studentId);

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
            if (g == null || g.getGradePoint() == null || g.getCredits() == null) continue;
            totalGradePointSum += (g.getGradePoint() * g.getCredits());
            totalCredits += g.getCredits();
        }

        double totalGpa = totalCredits == 0 ? 0.0 : Math.round((totalGradePointSum / totalCredits) * 100.0) / 100.0;

        return GradeSummaryRes.builder()
                .totalGpa(totalGpa)
                .totalCredits(totalCredits)
                .semesterGrades(grades)
                .build();
    }

    /**
     * 학생의 출결 현황 조회
     */
    public List<AcademicAttendanceRes> getAttendance(long studentId) {
        try {
            return academicMapper.findAttendanceByStudentId(studentId);
        } catch (Exception e) {
            // 테이블이 없거나 SQL 에러 발생 시 빈 리스트 반환 (서버 다운 방지)
            return List.of();
        }
    }
}
