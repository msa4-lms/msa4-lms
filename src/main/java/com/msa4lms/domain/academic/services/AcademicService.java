package com.msa4lms.domain.academic.services;

import com.msa4lms.domain.academic.mapper.AcademicMapper;
import com.msa4lms.domain.academic.requests.ExcuseDecisionReq;
import com.msa4lms.domain.academic.requests.ExcuseRequestReq;
import com.msa4lms.domain.academic.responses.AcademicAttendanceRes;
import com.msa4lms.domain.academic.responses.AttendanceRateRes;
import com.msa4lms.domain.academic.responses.ExcuseRequestRes;
import com.msa4lms.domain.academic.responses.GradeSummaryRes;
import com.msa4lms.domain.academic.responses.SemesterGradeRes;
import com.msa4lms.global.errors.custom.NotRegisteredException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicService {
    private final AcademicMapper academicMapper;
    private final JdbcTemplate jdbcTemplate;

    /**
     * 학생의 전체 성적 요약 및 평점 계산
     */
    public GradeSummaryRes getGradeSummary(long studentId, Integer year, Integer semester) {
        List<SemesterGradeRes> grades = academicMapper.findGradesByStudentId(studentId, year, semester);

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
        } catch (BadSqlGrammarException e) {
            return List.of();
        }
    }

    /**
     * 학생의 과목별 출석률 조회
     */
    public List<AttendanceRateRes> getAttendanceRates(long studentId, Integer year, Integer semester) {
        try {
            return academicMapper.findAttendanceRatesByStudentId(studentId, year, semester);
        } catch (BadSqlGrammarException e) {
            return List.of();
        }
    }

    /**
     * 학생 공결 신청
     */
    public void requestExcuse(long studentId, ExcuseRequestReq req) {
        Integer ownedCount = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM enrollments e
                JOIN students s ON e.student_id = s.id
                WHERE e.id = ?
                  AND s.user_id = ?
                """,
                Integer.class,
                req.enrollmentId(),
                studentId);
        if (ownedCount == 0) {
            throw new NotRegisteredException("본인의 수강 내역에만 공결을 신청할 수 있습니다.");
        }

        academicMapper.insertExcuseRequest(
                studentId,
                req.enrollmentId(),
                req.lectureDate().toString(),
                req.period(),
                req.reason());
    }

    /**
     * 학생 공결 승인 결과 조회
     */
    public List<ExcuseRequestRes> getMyExcuseRequests(long studentId) {
        try {
            return academicMapper.findExcuseRequestsByStudentId(studentId);
        } catch (BadSqlGrammarException e) {
            return List.of();
        }
    }

    /**
     * 교수 공결 승인 대기 목록 조회
     */
    public List<ExcuseRequestRes> getPendingExcuseRequests(long professorId) {
        try {
            return academicMapper.findPendingExcuseRequestsByProfessorId(professorId);
        } catch (BadSqlGrammarException e) {
            return List.of();
        }
    }

    /**
     * 교수 공결 승인/반려 처리
     */
    public void decideExcuseRequest(long professorId, long requestId, ExcuseDecisionReq req) {
        String status = req.status().toUpperCase();
        if (!status.equals("APPROVED") && !status.equals("REJECTED")) {
            throw new NotRegisteredException("승인 상태는 APPROVED 또는 REJECTED만 가능합니다.");
        }

        int updatedCount = academicMapper.updateExcuseRequestStatus(
                professorId,
                requestId,
                status,
                req.rejectReason());
        if (updatedCount == 0) {
            throw new NotRegisteredException("처리할 공결 신청을 찾을 수 없습니다.");
        }

        if (status.equals("APPROVED")) {
            academicMapper.applyApprovedExcuse(requestId);
        }
    }
}
