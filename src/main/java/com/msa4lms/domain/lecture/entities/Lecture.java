package com.msa4lms.domain.lecture.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 개설 강좌 정보를 담는 엔티티 클래스입니다.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {
    private Long id;
    private Long semesterId;
    private Long courseId;
    private Long professorId;
    private String sectionNo;
    private Integer capacity;
    private String classroom;
    private String status; // OPEN, CLOSED

    // 성적 평가 비율 (기본값 설정)
    private Integer midtermRatio;
    private Integer finalRatio;
    private Integer assignmentRatio;
    private Integer attendanceRatio;

    private String syllabus;
}
