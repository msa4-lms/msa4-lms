package com.msa4lms.domain.leaveReturn.services;

import com.msa4lms.domain.leaveReturn.mapper.LeaveReturnMapper;
import com.msa4lms.domain.leaveReturn.requests.LeaveReturnProcessReq;
import com.msa4lms.domain.leaveReturn.requests.LeaveReturnReq;
import com.msa4lms.domain.leaveReturn.responses.LeaveReturnRes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveReturnService {

    private final LeaveReturnMapper mapper;

    @Transactional
    public void submitRequest(Long userId, LeaveReturnReq req) {
        if (!"LEAVE".equals(req.requestType()) && !"RETURN".equals(req.requestType())) {
            throw new IllegalArgumentException("유효하지 않은 신청 유형입니다.");
        }
        mapper.insertRequest(userId, req);
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
                String academicStatus = "LEAVE".equals(request.requestType()) ? "ON_LEAVE" : "ENROLLED";
                mapper.updateStudentAcademicStatus(userId, academicStatus);
            }
        }
    }
}
