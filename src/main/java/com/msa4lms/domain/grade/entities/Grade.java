package com.msa4lms.domain.grade.entities;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    private Long id;
    private Long enrollmentId;
    private BigDecimal score;         
    private String letterGrade;     
    private LocalDateTime updatedAt;
  
    private BigDecimal midtermScore;
    private BigDecimal finalScore;
    private BigDecimal assignmentScore;
    private BigDecimal attendanceScore;
   
    private String status;

    private String objectionReason;
    private String objectionReply;

    private String studentName;
    private String studentLoginId;
}

