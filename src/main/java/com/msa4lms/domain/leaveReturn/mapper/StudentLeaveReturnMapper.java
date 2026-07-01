package com.msa4lms.domain.leaveReturn.mapper;

import com.msa4lms.domain.leaveReturn.requests.LeaveReturnReq;
import com.msa4lms.domain.leaveReturn.responses.LeaveReturnRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StudentLeaveReturnMapper {
    void insertRequest(@Param("userId") Long userId, @Param("req") LeaveReturnReq req, @Param("filePath") String filePath);
    List<LeaveReturnRes> findRequestsByUserId(@Param("userId") Long userId);
    String findAcademicStatusByUserId(@Param("userId") Long userId);
}
