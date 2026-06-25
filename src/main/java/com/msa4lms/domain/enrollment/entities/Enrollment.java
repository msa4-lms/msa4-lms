package com.msa4lms.domain.enrollment.entities;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * 수강 신청 정보를 담는 엔티티 클래스입니다.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    private Long id;
    private Long studentId;
    private Long lectureId;
    private String status; // ACTIVE, DROPPED
    private LocalDateTime enrolledAt;

    // 성적 관련 필드 (lms.sql 스키마 동기화)
    private java.math.BigDecimal midtermScore;
    private java.math.BigDecimal finalScore;
    private java.math.BigDecimal assignmentScore;
    private java.math.BigDecimal attendanceScore;
    private java.math.BigDecimal totalScore;
    private String letterGrade;
    private String gradeStatus; // DRAFT, 등등
    private String objectionReply;
    private String grade;
    // status 필드 중복 제거

    // 怨듯넻 ?꾨뱶 (而⑤깽??以??
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}

