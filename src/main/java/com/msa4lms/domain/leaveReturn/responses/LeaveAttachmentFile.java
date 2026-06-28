package com.msa4lms.domain.leaveReturn.responses;

import org.springframework.core.io.Resource;

public record LeaveAttachmentFile(
        Resource resource,
        String originalName,
        String contentType
) {}
