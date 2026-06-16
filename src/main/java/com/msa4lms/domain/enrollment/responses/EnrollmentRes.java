package com.msa4lms.domain.enrollment.responses;

import java.util.List;

/**
 * 학생의 수강 내역 목록과 총 신청 학점을 응답하기 위한 DTO입니다.
 * GEMINI.md 지침에 따라 Java Record를 사용합니다.
 */
public record EnrollmentRes(
    List<EnrollmentDetail> enrollments, // 수강 신청 과목 리스트
    int totalCredits                   // 신청 과목 총 합계 학점
) {
    /**
     * 개별 수강 과목의 상세 정보입니다.
     */
    public record EnrollmentDetail(
        Long id,               // 수강신청 식별자 (PK)
        String courseCode,     // 과목 코드 (예: CS1001)
        String courseName,     // 과목명
        String professorName,  // 교수님 성함
        String classroom,      // 강의실
        String schedule,       // 강의 시간 (예: 월1)
        int credits,           // 학점
        String status          // 수강 상태 (ENROLLED, DROPPED 등)
    ) {}
}
