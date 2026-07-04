package com.msa4lms.domain.attendance.services;

import com.msa4lms.domain.attendance.responses.ExcuseAttachmentFile;
import com.msa4lms.global.errors.custom.FileManagedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * 공결 첨부파일 조회 결과(Map row)를 실제 파일 리소스로 변환하는 공용 헬퍼.
 * 학생/교수 출결 서비스가 동일하게 사용하던 경로 검증 + 리소스 생성 로직을 한곳으로 모은다.
 */
@Component
public class ExcuseAttachmentResolver {

    @Value("${storage.excuse-attachments}")
    private String excuseAttachmentPath;

    public ExcuseAttachmentFile resolve(Map<String, Object> row) {
        String storedName = String.valueOf(row.get("attachmentStoredName"));
        Path storageRoot = Paths.get(excuseAttachmentPath).toAbsolutePath().normalize();
        Path filePath = storageRoot.resolve(storedName).normalize();
        if (!filePath.startsWith(storageRoot) || !Files.isRegularFile(filePath)) {
            throw new FileManagedException("첨부파일을 찾을 수 없습니다.");
        }

        String originalName = String.valueOf(row.get("attachmentOriginalName"));
        Object contentTypeValue = row.get("attachmentContentType");
        String contentType = contentTypeValue == null
                ? "application/octet-stream"
                : String.valueOf(contentTypeValue);

        return new ExcuseAttachmentFile(new FileSystemResource(filePath), originalName, contentType);
    }
}
