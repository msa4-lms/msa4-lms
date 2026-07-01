package com.msa4lms.domain.dashboard.services;

import com.msa4lms.domain.dashboard.mapper.DashboardMapper;
import com.msa4lms.domain.dashboard.responses.AcademicScheduleRes;
import com.msa4lms.domain.dashboard.responses.NoticeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardMapper dashboardMapper;

    // schedule
    public List<AcademicScheduleRes> getSchedules (String role) {
        return dashboardMapper.findScheduleByRole(role);

    }

    // notice
    public List<NoticeRes> getNotices (String role) {
        return dashboardMapper.findNoticeByRole(role);
    }


}
