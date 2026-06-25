package com.msa4lms.domain.leaveReturn.services;

import com.msa4lms.domain.leaveReturn.mapper.LeaveReturnMapper;
import com.msa4lms.domain.leaveReturn.requests.LeaveReturnProcessReq;
import com.msa4lms.domain.leaveReturn.requests.LeaveReturnReq;
import com.msa4lms.domain.leaveReturn.responses.LeaveReturnRes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveReturnService {

    private final LeaveReturnMapper mapper;

    @Transactional
    public void submitRequest(Long userId, LeaveReturnReq req, MultipartFile file) {
        if (!"GENERAL_LEAVE".equals(req.requestType()) && !"GENERAL_RETURN".equals(req.requestType()) &&
            !"MILITARY_LEAVE".equals(req.requestType()) && !"MILITARY_RETURN".equals(req.requestType()) &&
            !"LEAVE".equals(req.requestType()) && !"RETURN".equals(req.requestType())) {
            throw new IllegalArgumentException("유효하지 않은 신청 유형입니다.");
        }

        String filePath = null;
        if (file != null && !file.isEmpty()) {
            try {
                String uploadDir = "uploads/academic-requests/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String originalFileName = file.getOriginalFilename();
                String extension = "";
                if (originalFileName != null && originalFileName.contains(".")) {
                    extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                }

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                String datePrefix = LocalDate.now().format(dateFormatter);
                String fileName = datePrefix + "_" + UUID.randomUUID().toString() + extension;

                Path destPath = uploadPath.resolve(fileName);
                file.transferTo(destPath.toFile());
                filePath = destPath.toString().replace("\\", "/");
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드에 실패했습니다.", e);
            }
        }

        mapper.insertRequest(userId, req, filePath);
    }

    public List<LeaveReturnRes> getMyRequests(Long userId) {
        return mapper.findRequestsByUserId(userId);
    }

    public List<LeaveReturnRes> getPendingRequests() {
        return mapper.findAllPendingRequests();
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
