package com.msa4lms.domain.attendance.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record ExcuseRequestRes(
    Long id,
    Long enrollmentId,
    String courseName,
    String studentName,
    String studentNo,
    String lectureDate,
    Integer period,
    String reason,
    String status,
    String rejectReason,
    String attachmentOriginalName,
    String attachmentContentType,
    Long attachmentSize
) {}
