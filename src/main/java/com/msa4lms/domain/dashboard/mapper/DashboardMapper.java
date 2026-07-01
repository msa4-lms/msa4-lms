package com.msa4lms.domain.dashboard.mapper;

import com.msa4lms.domain.dashboard.responses.AcademicScheduleRes;
import com.msa4lms.domain.dashboard.responses.NoticeRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DashboardMapper {
    List<AcademicScheduleRes> findScheduleByRole(String role); // 여러 개 받아야 하니 List

    List<NoticeRes> findNoticeByRole(String role);
}
