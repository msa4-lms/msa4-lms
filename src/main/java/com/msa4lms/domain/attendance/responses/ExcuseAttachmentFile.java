package com.msa4lms.domain.attendance.responses;

import org.springframework.core.io.Resource;

public record ExcuseAttachmentFile(
        Resource resource,
        String originalName,
        String contentType
) {}
