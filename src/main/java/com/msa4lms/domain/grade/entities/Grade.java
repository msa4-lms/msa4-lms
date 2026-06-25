package com.msa4lms.domain.grade.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 성적 및 이의신청 정보를 담는 엔티티 클래스입니다.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    private Long id;
    private Long enrollmentId;
    private BigDecimal score;         // 총점
    private String letterGrade;      // 등급 (A+, A, B+ 등)
    private LocalDateTime updatedAt;

    // 상세 성적 점수
    private BigDecimal midtermScore;
    private BigDecimal finalScore;
    private BigDecimal assignmentScore;
    private BigDecimal attendanceScore;

    // 성적 상태 (DRAFT, SUBMITTED, OPENED, OBJECTION, APPROVED, FINAL)
    private String status;

    // 이의신청 사유 및 교수 답변
    private String objectionReason;
    private String objectionReply;

    // 수강생 추가 정보 (임시 조인 필드)
    private String studentName;
    private String studentLoginId;
}
