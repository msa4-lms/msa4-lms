package com.msa4lms.domain.enrollment.dto;

import java.util.List;

/**
 * 학생의 수강 내역 목록과 총 신청 학점을 응답하기 위한 DTO입니다.
 * GEMINI.md 지침에 따라 Java Record를 사용합니다.
 *     record
 *    * 예 Getter, Setter, toString 등을 일일이 다 쓸 필요 없이. record를 쓰면 이 모든 게 자동으로 생성됩니다.
 *    * 얻는 것: 코드의 간결함과 불변성. 한 번 데이터가 담기면 바뀌지 않도록 보장해 줍니다.
 *    (발표 때 "데이터의 신뢰성을 위해 불변 객체인 Record를 선택했습니다"라고 하면 아주 좋습니다.)
 */
public record MyEnrollmentListRes(
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
        String room,           // 강의실
        String schedule,       // 강의 시간 (예: 월1)
        int credits,           // 학점
        String status          // 수강 상태 (ENROLLED, DROPPED 등)
    ) {}
}


//[Step 1 완료 및 리뷰]
// * 왜 이렇게 짰나요?
// * 단순히 리스트만 보내는 게 아니라, 사용자님이 맡으신 기능 중 하나인 "신청 과목 합계 학점"을 totalCredits 필드로 한 번에
// 보내주도록 설계했습니다. 이렇게 하면 프론트엔드에서 따로 계산할 필요 없이 바로 보여줄 수 있어 효율적입니다.
// * static record 형태를 중첩하여 EnrollmentDetail을 정의함으로써, "수강 목록 응답"과 관련된 데이터 구조임을 명확히 했습니다.