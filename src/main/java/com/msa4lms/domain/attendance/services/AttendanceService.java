package com.msa4lms.domain.attendance.services;

import com.msa4lms.domain.attendance.entities.Attendance;
import com.msa4lms.domain.attendance.mapper.AttendanceMapper;
import com.msa4lms.domain.attendance.requests.PostAttendanceReq;
import com.msa4lms.domain.attendance.responses.AttendanceRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {

    private final AttendanceMapper attendanceMapper;

    @Transactional
    public void saveAttendance(PostAttendanceReq req) {
        Attendance attendance = new Attendance();
        attendance.setEnrollmentId(req.enrollmentId());
        attendance.setLectureDate(req.lectureDate());
        attendance.setPeriod(req.period());
        attendance.setStatus(req.status());
        attendance.setRemarks(req.remarks());
        
        attendanceMapper.insertAttendance(attendance);
    }

    @Transactional
    public void updateAttendance(Long id, String status, String remarks) {
        Attendance attendance = attendanceMapper.findById(id);
        if (attendance != null) {
            attendance.setStatus(status);
            attendance.setRemarks(remarks);
            attendanceMapper.updateAttendance(attendance);
        }
    }

    public List<AttendanceRes> getMyAttendances(Long enrollmentId) {
        return attendanceMapper.findByEnrollmentId(enrollmentId);
    }

    public List<AttendanceRes> getLectureAttendances(Long lectureId, LocalDate date) {
        return attendanceMapper.findByLectureIdAndDate(lectureId, date);
    }
}
