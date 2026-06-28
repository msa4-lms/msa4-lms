package com.msa4lms.domain.leaveReturn.controllers;

import com.msa4lms.domain.leaveReturn.requests.LeaveReturnProcessReq;
import com.msa4lms.domain.leaveReturn.responses.LeaveAttachmentFile;
import com.msa4lms.domain.leaveReturn.responses.LeaveReturnRes;
import com.msa4lms.domain.leaveReturn.services.ProfessorLeaveReturnService;
import com.msa4lms.global.responses.GlobalRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/professor/academic-requests")
@RequiredArgsConstructor
public class ProfessorLeaveReturnController {

    private final ProfessorLeaveReturnService service;

    @GetMapping("/pending")
    public ResponseEntity<GlobalRes<List<LeaveReturnRes>>> getPendingRequests() {
        return ResponseEntity.ok(
            GlobalRes.<List<LeaveReturnRes>>builder()
                .code("00")
                .message("조회가 완료되었습니다.")
                .data(service.getPendingRequests())
                .build()
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<GlobalRes<Void>> processRequest(
            @PathVariable("id") Long id,
            @RequestBody @Valid LeaveReturnProcessReq req
    ) {
        service.processRequest(id, req);
        return ResponseEntity.ok(
            GlobalRes.<Void>builder()
                .code("00")
                .message("처리가 완료되었습니다.")
                .build()
        );
    }

    @GetMapping("/{id}/attachment")
    public ResponseEntity<Resource> getAttachment(@PathVariable("id") Long id) {
        LeaveAttachmentFile file = service.getAttachment(id);

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(file.contentType());
        } catch (IllegalArgumentException e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        boolean inline = MediaType.APPLICATION_PDF.includes(mediaType)
                || "image".equalsIgnoreCase(mediaType.getType());
        ContentDisposition disposition = (inline
                ? ContentDisposition.inline()
                : ContentDisposition.attachment())
                .filename(file.originalName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(file.resource());
    }
}
