package com.msa4lms.domain.enrollment.entities;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * 수강 신청 정보를 담는 엔티티 클래스입니다.
 */
@Getter
@Setter
public class Enrollment {
    private Long id;
    private Long studentId;
    private Long lectureId;
    private String grade;
    private String status; // ENROLLED, COMPLETED, DROPPED
    
    // 공통 필드 (컨벤션 준수)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
