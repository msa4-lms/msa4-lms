package com.msa4lms.global.file;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @GetMapping
    public ResponseEntity<Resource> downloadFile(@RequestParam("path") String filePath) {
        try {
            Path path = Paths.get(filePath).normalize();
            // 보안을 위해 특정 디렉토리(uploads) 하위에 있는 파일만 허용
            if (!path.toString().contains("uploads")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                String originalFilename = resource.getFilename();
                String encodedFilename = URLEncoder.encode(originalFilename != null ? originalFilename : "downloaded_file", StandardCharsets.UTF_8).replace("+", "%20");

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
