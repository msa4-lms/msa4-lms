package com.msa4lms.domain.leaveReturn.services;

import com.msa4lms.domain.leaveReturn.mapper.StudentLeaveReturnMapper;
import com.msa4lms.domain.leaveReturn.requests.LeaveReturnProcessReq;
import com.msa4lms.domain.leaveReturn.requests.LeaveReturnReq;
import com.msa4lms.domain.leaveReturn.responses.LeaveReturnRes;
import com.msa4lms.global.errors.custom.DuplicatedRecordException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class StudentLeaveReturnService {

    private final StudentLeaveReturnMapper mapper;

    @Value("${storage.academic-attachments}")
    private String academicAttachmentPath;

    @Transactional
    public void submitRequest(Long userId, LeaveReturnReq req, MultipartFile file) {
        if (!"GENERAL_LEAVE".equals(req.requestType()) && !"GENERAL_RETURN".equals(req.requestType()) &&
            !"MILITARY_LEAVE".equals(req.requestType()) && !"MILITARY_RETURN".equals(req.requestType()) &&
            !"LEAVE".equals(req.requestType()) && !"RETURN".equals(req.requestType())) {
            throw new IllegalArgumentException("유효하지 않은 신청 유형입니다.");
        }

        List<LeaveReturnRes> existingRequests = getMyRequests(userId);

        // 현재 군휴학 상태 계산: 군휴학이 심사 대기(PENDING)이거나, 승인되었고 아직 군복학(MILITARY_RETURN)이 승인되지 않은 경우
        boolean pendingMilitaryLeave = existingRequests.stream()
                .anyMatch(r -> "MILITARY_LEAVE".equals(r.requestType()) && "PENDING".equals(r.status()));
        long approvedMilitaryLeave = existingRequests.stream()
                .filter(r -> "MILITARY_LEAVE".equals(r.requestType()) && "APPROVED".equals(r.status()))
                .count();
        long approvedMilitaryReturn = existingRequests.stream()
                .filter(r -> "MILITARY_RETURN".equals(r.requestType()) && "APPROVED".equals(r.status()))
                .count();
        boolean onMilitaryLeave = pendingMilitaryLeave || (approvedMilitaryLeave > approvedMilitaryReturn);

        // 1) 같은 유형의 대기중(PENDING) 신청 중복 방지 (군휴학은 다른 유형의 대기 신청과 무관하게 허용 → 일반→군휴학 전환 가능)
        boolean hasSameTypePending = existingRequests.stream()
                .anyMatch(r -> "PENDING".equals(r.status()) && req.requestType().equals(r.requestType()));
        if (hasSameTypePending) {
            throw new DuplicatedRecordException("이미 대기 중인 동일 유형의 신청 내역이 존재합니다. 관리자의 처리를 기다려주세요.");
        }

        // 2) 군휴학은 재학 중 단 한 번만 신청 가능 (이미 신청 대기중이거나 한 번이라도 승인된 이력이 있으면 차단)
        if ("MILITARY_LEAVE".equals(req.requestType())) {
            boolean militaryLeaveUsed = existingRequests.stream()
                    .anyMatch(r -> "MILITARY_LEAVE".equals(r.requestType())
                            && ("PENDING".equals(r.status()) || "APPROVED".equals(r.status())));
            if (militaryLeaveUsed) {
                throw new DuplicatedRecordException("군휴학은 재학 중 한 번만 신청할 수 있습니다.");
            }
        }

        // 3) 군휴학 중(승인 후 미복학 또는 심사 대기)에는 일반휴학을 신청할 수 없음 (군휴학 기간이 더 길어 일반휴학을 포함)
        boolean isGeneralLeave = req.requestType().contains("LEAVE") && !req.requestType().contains("MILITARY");
        if (isGeneralLeave && onMilitaryLeave) {
            throw new DuplicatedRecordException("군휴학 중에는 일반휴학을 신청할 수 없습니다. (군휴학 기간이 일반휴학 기간을 포함합니다.)");
        }

        // 4) 복학은 휴학 중인 학생만 신청 가능 (재학/졸업 상태에서는 복학 불가)
        if (req.requestType().contains("RETURN")) {
            String academicStatus = mapper.findAcademicStatusByUserId(userId);
            if (!"ON_LEAVE".equals(academicStatus)) {
                throw new DuplicatedRecordException("휴학 중인 경우에만 복학을 신청할 수 있습니다.");
            }
        }

        // 5) 복학 신청은 단일 화면에서 받되, 현재 군휴학 중이면 군복학(MILITARY_RETURN)으로 자동 전환하여 저장
        LeaveReturnReq effectiveReq = req;
        if (req.requestType().contains("RETURN") && onMilitaryLeave) {
            effectiveReq = new LeaveReturnReq(
                    "MILITARY_RETURN",
                                req.reason(),
                                req.targetYear(),
                                req.targetSemester(),
                                req.returnYear(),
                                req.returnSemester());
                    }

        String filePath = null;
        if (file != null && !file.isEmpty()) {
            try {
                Path uploadPath = Paths.get(academicAttachmentPath);
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

                Path destPath = uploadPath.resolve(fileName).toAbsolutePath();
                file.transferTo(destPath.toFile());
                filePath = destPath.toString().replace("\\", "/");
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드에 실패했습니다.", e);
            }
        }

        mapper.insertRequest(userId, effectiveReq, filePath);
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
