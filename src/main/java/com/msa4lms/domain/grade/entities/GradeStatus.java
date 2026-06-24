package com.msa4lms.domain.grade.entities;

/**
 * 성적 상태를 나타내는 Enum 클래스입니다.
 */
public enum GradeStatus {
    DRAFT,       // 임시저장
    SUBMITTED,   // 제출됨 (학생 조회 가능 상태 직전)
    OPENED,      // 공개됨 (학생 성적 조회 가능)
    OBJECTION,   // 이의신청
    APPROVED,    // 이의신청 승인/반영됨
    FINAL        // 최종확정
}
