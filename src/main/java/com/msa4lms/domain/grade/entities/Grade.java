package com.msa4lms.domain.grade.entities;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ?깆쟻 諛??댁쓽?좎껌 ?뺣낫瑜??대뒗 ?뷀떚???대옒?ㅼ엯?덈떎.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    private Long id;
    private Long enrollmentId;
    private BigDecimal score;         // 珥앹젏
    private String letterGrade;      // ?깃툒 (A+, A, B+ ??
    private LocalDateTime updatedAt;

    // ?곸꽭 ?깆쟻 ?먯닔
    private BigDecimal midtermScore;
    private BigDecimal finalScore;
    private BigDecimal assignmentScore;
    private BigDecimal attendanceScore;

    // ?깆쟻 ?곹깭 (DRAFT, SUBMITTED, OPENED, OBJECTION, APPROVED, FINAL)
    private String status;

    // ?댁쓽?좎껌 ?ъ쑀 諛?援먯닔 ?듬?
    private String objectionReason;
    private String objectionReply;

    // ?섍컯??異붽? ?뺣낫 (?꾩떆 議곗씤 ?꾨뱶)
    private String studentName;
    private String studentLoginId;
}

