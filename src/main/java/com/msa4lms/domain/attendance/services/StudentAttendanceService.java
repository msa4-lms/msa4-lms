package com.msa4lms.domain.attendance.services;

import com.msa4lms.domain.attendance.mapper.StudentAttendanceMapper;
import com.msa4lms.domain.attendance.responses.AttendanceRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.Set;
import java.util.Map;
import java.util.Locale;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import com.msa4lms.global.errors.custom.FileManagedException;
import com.msa4lms.global.errors.custom.NotRegisteredException;
import com.msa4lms.domain.attendance.responses.ExcuseAttachmentFile;
import com.msa4lms.domain.attendance.responses.ExcuseRequestRes;
import com.msa4lms.domain.attendance.responses.AttendanceRateRes;
import com.msa4lms.domain.attendance.responses.AcademicAttendanceRes;
import com.msa4lms.domain.attendance.requests.ExcuseApplyReq;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentAttendanceService {

    private final StudentAttendanceMapper attendanceMapper;
    private final ExcuseAttachmentResolver excuseAttachmentResolver;

    @Value("${storage.excuse-attachments}")
    private String excuseAttachmentPath;

    private static final long MAX_ATTACHMENT_SIZE = 10L * 1024 * 1024;
    private static final Set<String> ALLOWED_ATTACHMENT_EXTENSIONS = Set.of("pdf", "jpg", "jpeg", "png", "hwp", "hwpx");



    public List<AttendanceRes> getMyAttendances(Long enrollmentId) {
        return attendanceMapper.findByEnrollmentId(enrollmentId);
    }


    /**
     * 학생의 출결 현황 조회
     */
    public List<AcademicAttendanceRes> getAttendance(long studentId) {
        return attendanceMapper.findAttendanceByStudentId(studentId);
    }

    /**
     * 학생의 과목별 출석률 조회
     */
    public List<AttendanceRateRes> getAttendanceRates(long studentId, Integer year, Integer semester) {
        return attendanceMapper.findAttendanceRatesByStudentId(studentId, year, semester);
    }

    /**
     * 학생 공결 신청
     */
    @Transactional
    public void requestExcuse(long studentId, ExcuseApplyReq req) {
        requestExcuse(studentId, req, null);
    }

    @Transactional
    public void requestExcuse(long studentId, ExcuseApplyReq req, MultipartFile attachment) {
        int ownedCount = attendanceMapper.countOwnedEnrollment(studentId, req.enrollmentId());
        if (ownedCount == 0) {
            throw new NotRegisteredException("본인의 수강 내역에만 공결을 신청할 수 있습니다.");
        }

        int duplicateCount = attendanceMapper.countExcuseByDateAndPeriod(
                req.enrollmentId(), req.lectureDate().toString(), req.period());
        if (duplicateCount > 0) {
            throw new NotRegisteredException("이미 공결을 신청한 일정입니다.");
        }

        StoredAttachment storedAttachment = storeAttachment(attachment);
        try {
            attendanceMapper.insertExcuseRequest(
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



    public ExcuseAttachmentFile getStudentExcuseAttachment(long studentId, long requestId) {
        Map<String, Object> file = attendanceMapper.findStudentExcuseAttachment(requestId, studentId);
        if (file == null) {
            throw new NotRegisteredException("열람할 수 있는 첨부파일이 없습니다.");
        }
        return excuseAttachmentResolver.resolve(file);
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
        return attendanceMapper.findExcuseRequestsByStudentId(studentId);
    }

}
