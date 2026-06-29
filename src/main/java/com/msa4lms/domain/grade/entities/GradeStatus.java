package com.msa4lms.domain.grade.entities;

/**
 * 성적 상태를 나타내는 Enum 클래스입니다.
 * DB 실사용 값: DRAFT, OPENED
 * FINAL: 최종확정 처리 용도로 서비스 레이어에서 참조 (2차 구현 예정)
 */
public enum GradeStatus {
    DRAFT,   // 임시저장 (교수 입력 중, 학생 조회 불가)
    OPENED,  // 공개됨 (학생 성적 조회 가능)
    FINAL    // 최종확정 (정정 불가 - 2차 구현 예정)
}
