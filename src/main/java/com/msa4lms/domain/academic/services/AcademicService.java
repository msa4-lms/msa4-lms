package com.msa4lms.domain.academic.services;

import com.msa4lms.domain.academic.mapper.AcademicMapper;
import com.msa4lms.domain.academic.requests.ExcuseDecisionReq;
import com.msa4lms.domain.academic.requests.ExcuseRequestReq;
import com.msa4lms.domain.academic.responses.AcademicAttendanceRes;
import com.msa4lms.domain.academic.responses.AttendanceRateRes;
import com.msa4lms.domain.academic.responses.ExcuseRequestRes;
import com.msa4lms.domain.academic.responses.ExcuseAttachmentFile;
import com.msa4lms.domain.academic.responses.GradeSummaryRes;
import com.msa4lms.domain.academic.responses.SemesterGradeRes;
import com.msa4lms.global.errors.custom.NotRegisteredException;
import com.msa4lms.global.errors.custom.FileManagedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AcademicService {
    private final AcademicMapper academicMapper;
    private final JdbcTemplate jdbcTemplate;

    @Value("${storage.excuse-attachments}")
    private String excuseAttachmentPath;

    private static final long MAX_ATTACHMENT_SIZE = 10L * 1024 * 1024;
    private static final Set<String> ALLOWED_ATTACHMENT_EXTENSIONS =
            Set.of("pdf", "jpg", "jpeg", "png", "hwp", "hwpx");

    /**
     * 학생의 전체 성적 요약 및 평점 계산
     */
    public GradeSummaryRes getGradeSummary(long studentId, Integer year, Integer semester) {
        List<SemesterGradeRes> grades = academicMapper.findGradesByStudentId(studentId, year, semester);

        if (grades == null || grades.isEmpty()) {
            return GradeSummaryRes.builder()
                    .totalGpa(0.0)
                    .totalCredits(0)
                    .semesterGrades(List.of())
                    .build();
        }

        double totalGradePointSum = 0;
        int totalCredits = 0;

        for (SemesterGradeRes g : grades) {
            if (g == null || g.getGradePoint() == null || g.getCredits() == null) continue;
            totalGradePointSum += (g.getGradePoint() * g.getCredits());
            totalCredits += g.getCredits();
        }

        double totalGpa = totalCredits == 0 ? 0.0 : Math.round((totalGradePointSum / totalCredits) * 100.0) / 100.0;

        return GradeSummaryRes.builder()
                .totalGpa(totalGpa)
                .totalCredits(totalCredits)
                .semesterGrades(grades)
                .build();
    }

    /**
     * 학생의 출결 현황 조회
     */
    public List<AcademicAttendanceRes> getAttendance(long studentId) {
        try {
            return academicMapper.findAttendanceByStudentId(studentId);
        } catch (BadSqlGrammarException e) {
            return List.of();
        }
    }

    /**
     * 학생의 과목별 출석률 조회
     */
    public List<AttendanceRateRes> getAttendanceRates(long studentId, Integer year, Integer semester) {
        try {
            return academicMapper.findAttendanceRatesByStudentId(studentId, year, semester);
        } catch (BadSqlGrammarException e) {
            return List.of();
        }
    }

    /**
     * 학생 공결 신청
     */
    @Transactional
    public void requestExcuse(long studentId, ExcuseRequestReq req) {
        requestExcuse(studentId, req, null);
    }

    @Transactional
    public void requestExcuse(long studentId, ExcuseRequestReq req, MultipartFile attachment) {
        Integer ownedCount = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM enrollments e
                JOIN students s ON e.student_id = s.id
                WHERE e.id = ?
                  AND s.user_id = ?
                """,
                Integer.class,
                req.enrollmentId(),
                studentId);
        if (ownedCount == 0) {
            throw new NotRegisteredException("본인의 수강 내역에만 공결을 신청할 수 있습니다.");
        }

        Integer duplicateCount = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM excuse_requests
                WHERE student_id = ?
                  AND enrollment_id = ?
                  AND lecture_date = ?
                  AND period = ?
                """,
                Integer.class,
                studentId,
                req.enrollmentId(),
                req.lectureDate(),
                req.period());
        if (duplicateCount != null && duplicateCount > 0) {
            throw new NotRegisteredException("이미 공결을 신청한 일정입니다.");
        }

        StoredAttachment storedAttachment = storeAttachment(attachment);
        try {
            academicMapper.insertExcuseRequest(
                    studentId,
                    req.enrollmentId(),
                    req.lectureDate().toString(),
                    req.period(),
                    req.reason(),
                    storedAttachment.originalName(),
                    storedAttachment.storedName(),
                    storedAttachment.contentType(),
                    storedAttachment.size());
        } catch (RuntimeException e) {
            deleteStoredAttachment(storedAttachment.storedName());
            throw e;
        }
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
        Path storageRoot = getAttachmentStorageRoot();
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

    public ExcuseAttachmentFile getStudentExcuseAttachment(long studentId, long requestId) {
        List<Map<String, Object>> files = jdbcTemplate.queryForList(
                """
                SELECT
                    attachment_original_name,
                    attachment_stored_name,
                    attachment_content_type
                FROM excuse_requests
                WHERE id = ?
                  AND student_id = ?
                  AND attachment_stored_name IS NOT NULL
                """,
                requestId,
                studentId);

        if (files.isEmpty()) {
            throw new NotRegisteredException("열람할 수 있는 첨부파일이 없습니다.");
        }

        return resolveExcuseAttachment(files.get(0));
    }

    private ExcuseAttachmentFile resolveExcuseAttachment(Map<String, Object> file) {
        String storedName = String.valueOf(file.get("attachment_stored_name"));
        Path storageRoot = getAttachmentStorageRoot();
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

    private StoredAttachment storeAttachment(MultipartFile attachment) {
        if (attachment == null || attachment.isEmpty()) {
            return StoredAttachment.empty();
        }
        if (attachment.getSize() > MAX_ATTACHMENT_SIZE) {
            throw new FileManagedException("첨부파일은 10MB 이하만 업로드할 수 있습니다.");
        }

        String originalName = attachment.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            throw new FileManagedException("첨부파일 이름을 확인할 수 없습니다.");
        }
        originalName = Paths.get(originalName).getFileName().toString();
        String extension = getExtension(originalName);
        if (!ALLOWED_ATTACHMENT_EXTENSIONS.contains(extension)) {
            throw new FileManagedException("PDF, JPG, PNG, HWP, HWPX 파일만 첨부할 수 있습니다.");
        }

        String storedName = UUID.randomUUID() + "." + extension;
        Path target = getAttachmentStorageRoot().resolve(storedName).normalize();
        try {
            Files.createDirectories(target.getParent());
            Files.copy(attachment.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileManagedException("첨부파일 저장에 실패했습니다.");
        }

        String contentType = attachment.getContentType() == null
                ? "application/octet-stream"
                : attachment.getContentType();
        return new StoredAttachment(originalName, storedName, contentType, attachment.getSize());
    }

    private Path getAttachmentStorageRoot() {
        return Paths.get(excuseAttachmentPath).toAbsolutePath().normalize();
    }

    private String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }

    private void deleteStoredAttachment(String storedName) {
        if (storedName == null) return;
        try {
            Files.deleteIfExists(getAttachmentStorageRoot().resolve(storedName).normalize());
        } catch (IOException ignored) {
        }
    }

    private record StoredAttachment(
            String originalName,
            String storedName,
            String contentType,
            Long size
    ) {
        private static StoredAttachment empty() {
            return new StoredAttachment(null, null, null, null);
        }
    }

    /**
     * 학생 공결 승인 결과 조회
     */
    public List<ExcuseRequestRes> getMyExcuseRequests(long studentId) {
        try {
            return academicMapper.findExcuseRequestsByStudentId(studentId);
        } catch (BadSqlGrammarException e) {
            return List.of();
        }
    }

    /**
     * 교수 공결 승인 대기 목록 조회
     */
    public List<ExcuseRequestRes> getPendingExcuseRequests(long professorId) {
        try {
            return academicMapper.findPendingExcuseRequestsByProfessorId(professorId);
        } catch (BadSqlGrammarException e) {
            return List.of();
        }
    }

    public List<ExcuseRequestRes> getProfessorExcuseRequests(long professorId) {
        try {
            return academicMapper.findExcuseRequestsByProfessorId(professorId);
        } catch (BadSqlGrammarException e) {
            return List.of();
        }
    }

    /**
     * 교수 공결 승인/반려 처리
     */
    public void decideExcuseRequest(long professorId, long requestId, ExcuseDecisionReq req) {
        String status = req.status().toUpperCase();
        if (!status.equals("APPROVED") && !status.equals("REJECTED")) {
            throw new NotRegisteredException("승인 상태는 APPROVED 또는 REJECTED만 가능합니다.");
        }

        int updatedCount = academicMapper.updateExcuseRequestStatus(
                professorId,
                requestId,
                status,
                req.rejectReason());
        if (updatedCount == 0) {
            throw new NotRegisteredException("처리할 공결 신청을 찾을 수 없습니다.");
        }

        if (status.equals("APPROVED")) {
            academicMapper.applyApprovedExcuse(requestId);
        }
    }
}
