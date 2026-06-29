package com.msa4lms.domain.leaveReturn.services;

import com.msa4lms.domain.leaveReturn.mapper.StudentLeaveReturnMapper;
import com.msa4lms.domain.leaveReturn.requests.LeaveReturnProcessReq;
import com.msa4lms.domain.leaveReturn.responses.LeaveAttachmentFile;
import com.msa4lms.domain.leaveReturn.responses.LeaveReturnRes;
import com.msa4lms.global.errors.custom.FileManagedException;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorLeaveReturnService {

    private final StudentLeaveReturnMapper mapper;

    public List<LeaveReturnRes> getPendingRequests() {
        return mapper.findAllPendingRequests();
    }

    // 휴학 신청 첨부파일 다운로드 (교수/관리자 권한은 URL 보안으로 보장됨)
    public LeaveAttachmentFile getAttachment(Long requestId) {
        String storedPath = mapper.findAttachmentPathByRequestId(requestId);
        if (!StringUtils.hasText(storedPath)) {
            throw new FileManagedException("해당 신청에 첨부된 파일이 없습니다.");
        }

        Path path = Paths.get(storedPath).normalize();
        Resource resource = new FileSystemResource(path);
        if (!resource.exists() || !resource.isReadable()) {
            throw new FileManagedException("첨부파일을 찾을 수 없습니다.");
        }

        String contentType;
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            contentType = null;
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return new LeaveAttachmentFile(resource, path.getFileName().toString(), contentType);
    }

    @Transactional
    public void processRequest(Long requestId, LeaveReturnProcessReq req) {
        Long userId = mapper.findUserIdByRequestId(requestId);
        if (userId == null) {
            throw new IllegalArgumentException("해당 신청 내역을 찾을 수 없습니다.");
        }

        if (!"APPROVED".equals(req.status()) && !"REJECTED".equals(req.status())) {
            throw new IllegalArgumentException("올바른 처리 상태가 아닙니다.");
        }

        mapper.updateRequestStatus(requestId, req.status(), req.rejectReason());

        if ("APPROVED".equals(req.status())) {
            // Retrieve the request to know if it's LEAVE or RETURN
            List<LeaveReturnRes> reqs = mapper.findRequestsByUserId(userId);
            LeaveReturnRes request = reqs.stream().filter(r -> r.id().equals(requestId)).findFirst().orElse(null);
            
            if (request != null) {
                String academicStatus = request.requestType().contains("LEAVE") ? "ON_LEAVE" : "ENROLLED";
                mapper.updateStudentAcademicStatus(userId, academicStatus);
            }
        }
    }
}
