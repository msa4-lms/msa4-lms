package com.msa4lms.domain.leaveReturn.responses;

import java.time.LocalDateTime;

public record LeaveReturnRes(
    Long id,
    Long userId,
    String studentName,
    String studentLoginId,
    String departmentName,
    String requestType,
    String reason,
    Integer targetYear,
    Integer targetSemester,
    Integer returnYear,
    Integer returnSemester,
    String status,
    String rejectReason,
    LocalDateTime createdAt,
    String attachmentFilePath
) {}
