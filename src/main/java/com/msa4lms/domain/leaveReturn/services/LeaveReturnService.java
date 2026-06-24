package com.msa4lms.domain.leaveReturn.services;

import com.msa4lms.domain.leaveReturn.mapper.LeaveReturnMapper;

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

}
