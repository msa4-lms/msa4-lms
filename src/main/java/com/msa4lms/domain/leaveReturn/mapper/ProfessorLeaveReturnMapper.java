package com.msa4lms.domain.leaveReturn.mapper;

import com.msa4lms.domain.leaveReturn.responses.LeaveReturnRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfessorLeaveReturnMapper {
    List<LeaveReturnRes> findAllPendingRequests();
    List<LeaveReturnRes> findRequestsByUserId(@Param("userId") Long userId);
    Long findUserIdByRequestId(@Param("id") Long id);
    void updateRequestStatus(@Param("id") Long id, @Param("status") String status, @Param("rejectReason") String rejectReason);
    void updateStudentAcademicStatus(@Param("userId") Long userId, @Param("academicStatus") String academicStatus);
    String findAttachmentPathByRequestId(@Param("id") Long id);
}
