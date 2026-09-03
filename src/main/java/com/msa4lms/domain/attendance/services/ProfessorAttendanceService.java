package com.msa4lms.domain.attendance.services;

import com.msa4lms.domain.attendance.entities.Attendance;
import com.msa4lms.domain.attendance.mapper.ProfessorAttendanceMapper;
import com.msa4lms.domain.attendance.requests.AttendanceUpdateReq;
import com.msa4lms.domain.attendance.responses.AttendanceRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.msa4lms.global.errors.custom.NotRegisteredException;
import com.msa4lms.domain.attendance.responses.ExcuseAttachmentFile;
import com.msa4lms.domain.attendance.responses.ExcuseRequestRes;
import com.msa4lms.domain.attendance.requests.ExcuseDecisionReq;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfessorAttendanceService {

    private final ProfessorAttendanceMapper attendanceMapper;
    private final ExcuseAttachmentResolver excuseAttachmentResolver;

    @Transactional
    public void saveAttendance(AttendanceUpdateReq req) {
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
        // 허용된 출결 상태값만 사용 가능
        Set<String> allowedStatuses = Set.of("PRESENT", "LATE", "ABSENT", "EXCUSED");
        if (status == null || !allowedStatuses.contains(status)) {
            throw new IllegalArgumentException("허용되지 않은 출결 상태값입니다. (허용: PRESENT, LATE, ABSENT, EXCUSED)");
        }

        Attendance attendance = attendanceMapper.findById(id);
        if (attendance != null) {
            attendance.setStatus(status);
            attendance.setRemarks(remarks);
            attendanceMapper.updateAttendance(attendance);
        }
    }

    public List<AttendanceRes> getLectureAttendances(Long lectureId, LocalDate date) {
        return attendanceMapper.findByLectureIdAndDate(lectureId, date);
    }

    public ExcuseAttachmentFile getExcuseAttachment(long professorId, long requestId) {
        Map<String, Object> file = attendanceMapper.findProfessorExcuseAttachment(requestId, professorId);
        if (file == null) {
            throw new NotRegisteredException("열람할 수 있는 첨부파일이 없습니다.");
        }
        return excuseAttachmentResolver.resolve(file);
    }

    public List<ExcuseRequestRes> getPendingExcuseRequests(long professorId) {
        return attendanceMapper.findPendingExcuseRequestsByProfessorId(professorId);
    }

    public List<ExcuseRequestRes> getProfessorExcuseRequests(long professorId) {
        return attendanceMapper.findExcuseRequestsByProfessorId(professorId);
    }

    @Transactional
    public void decideExcuseRequest(long professorId, long requestId, ExcuseDecisionReq req) {
        String status = req.status().toUpperCase();
        if (!status.equals("APPROVED") && !status.equals("REJECTED")) {
            throw new NotRegisteredException("승인 상태는 APPROVED 또는 REJECTED만 가능합니다.");
        }

        int updatedCount = attendanceMapper.updateExcuseRequestStatus(
                professorId,
                requestId,
                status,
                req.rejectReason());
        if (updatedCount == 0) {
            throw new NotRegisteredException("처리할 공결 신청을 찾을 수 없습니다.");
        }

        if (status.equals("APPROVED")) {
            attendanceMapper.applyApprovedExcuse(requestId);
        }
    }
}
