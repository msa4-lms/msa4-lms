package com.msa4lms.domain.leaveReturn.mapper;

import com.msa4lms.domain.leaveReturn.requests.LeaveReturnReq;
import com.msa4lms.domain.leaveReturn.responses.LeaveReturnRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface LeaveReturnMapper {
    void insertRequest(@Param("userId") Long userId, @Param("req") LeaveReturnReq req);
    List<LeaveReturnRes> findRequestsByUserId(@Param("userId") Long userId);
    List<LeaveReturnRes> findAllPendingRequests();
    void updateRequestStatus(@Param("id") Long id, @Param("status") String status, @Param("rejectReason") String rejectReason);
    void updateStudentAcademicStatus(@Param("userId") Long userId, @Param("academicStatus") String academicStatus);
    Long findUserIdByRequestId(@Param("id") Long id);
}
