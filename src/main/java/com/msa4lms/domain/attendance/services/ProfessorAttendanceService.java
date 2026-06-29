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
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.beans.factory.annotation.Value;
import com.msa4lms.global.errors.custom.FileManagedException;
import com.msa4lms.global.errors.custom.NotRegisteredException;
import com.msa4lms.domain.attendance.responses.ExcuseAttachmentFile;
import com.msa4lms.domain.attendance.responses.ExcuseRequestRes;
import com.msa4lms.domain.attendance.requests.ExcuseDecisionReq;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfessorAttendanceService {

    private final ProfessorAttendanceMapper attendanceMapper;
    private final JdbcTemplate jdbcTemplate;

    @Value("${storage.excuse-attachments}")
    private String excuseAttachmentPath;

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
        List<Map<String, Object>> files = jdbcTemplate.queryForList(
                """
                SELECT
                    er.attachment_original_name,
                    er.attachment_stored_name,
                    er.attachment_content_type
                FROM excuse_requests er
                JOIN enrollments e ON er.enrollment_id = e.id
                JOIN lectures l ON e.lecture_id = l.id
                WHERE er.id = ?
                  AND l.professor_id = (SELECT id FROM professors WHERE user_id = ?)
                  AND er.attachment_stored_name IS NOT NULL
                """,
                requestId,
                professorId);

        if (files.isEmpty()) {
            throw new NotRegisteredException("열람할 수 있는 첨부파일이 없습니다.");
        }

        Map<String, Object> file = files.get(0);
        String storedName = String.valueOf(file.get("attachment_stored_name"));
        Path storageRoot = Paths.get(excuseAttachmentPath).toAbsolutePath().normalize();
        Path filePath = storageRoot.resolve(storedName).normalize();
        if (!filePath.startsWith(storageRoot) || !Files.isRegularFile(filePath)) {
            throw new FileManagedException("첨부파일을 찾을 수 없습니다.");
        }

        String originalName = String.valueOf(file.get("attachment_original_name"));
        Object contentTypeValue = file.get("attachment_content_type");
        String contentType = contentTypeValue == null
                ? "application/octet-stream"
                : String.valueOf(contentTypeValue);

        return new ExcuseAttachmentFile(new FileSystemResource(filePath), originalName, contentType);
    }

    public List<ExcuseRequestRes> getPendingExcuseRequests(long professorId) {
        try {
            return attendanceMapper.findPendingExcuseRequestsByProfessorId(professorId);
        } catch (BadSqlGrammarException e) {
            return List.of();
        }
    }

    public List<ExcuseRequestRes> getProfessorExcuseRequests(long professorId) {
        try {
            return attendanceMapper.findExcuseRequestsByProfessorId(professorId);
        } catch (BadSqlGrammarException e) {
            return List.of();
        }
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
