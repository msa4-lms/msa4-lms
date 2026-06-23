package com.msa4lms.global.errors;

import com.msa4lms.global.errors.custom.*;
import com.msa4lms.global.responses.GlobalRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotRegisteredException.class)
    public ResponseEntity<GlobalRes<String>> notRegisteredHandle(NotRegisteredException e) {
        return ResponseEntity.status(401).body(
                GlobalRes.<String>builder()
                        .code("E01")
                        .message("로그인 에러")
                        .data(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GlobalRes<String>> authenticationHandle(AuthenticationException e) {
        return ResponseEntity.status(401).body(
                GlobalRes.<String>builder()
                        .code("E02")
                        .message("인증이 필요합니다.")
                        .data("로그인이 필요한 서비스입니다")
                        .build()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GlobalRes<String>> accessDeniedHandle(AccessDeniedException e) {
        return ResponseEntity.status(403).body(
                GlobalRes.<String>builder()
                        .code("E03")
                        .message("UNAUTHORIZED_ERROR")
                        .data("권한이 부족합니다.")
                        .build()
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<GlobalRes<String>> invalidTokenHandle(InvalidTokenException e) {
        return ResponseEntity.status(401).body(
                GlobalRes.<String>builder()
                        .code("E04")
                        .message("토큰 이상")
                        .data(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<GlobalRes<String>> passwordMismatchHandle(
            PasswordMismatchException e
    ) {
        return ResponseEntity.status(400).body(
                GlobalRes.<String>builder()
                        .code("E05")
                        .message("비밀번호 확인 실패")
                        .data(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(PasswordChangeFailedException.class)
    public ResponseEntity<GlobalRes<String>> passwordChangeFailedHandle(
            PasswordChangeFailedException e
    ) {
        return ResponseEntity.status(400).body(
                GlobalRes.<String>builder()
                        .code("E06")
                        .message("비밀번호 변경 실패")
                        .data(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(PasswordSameException.class)
    public ResponseEntity<GlobalRes<String>> passwordSameHandle(
            PasswordSameException e
    ) {
        return ResponseEntity.status(400).body(
                GlobalRes.<String>builder()
                        .code("E07")
                        .message("동일한 비밀번호는 사용할 수 없습니다.")
                        .data(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(DeletedRecordException.class)
    public ResponseEntity<GlobalRes<String>> deletedRecordHandle(DeletedRecordException e) {
        return ResponseEntity.status(404).body(
            GlobalRes.<String>builder()
                .code("E10")
                .message("삭제된 데이터입니다.")
                .data(e.getMessage())
                .build()
        );
    }

    @ExceptionHandler(DuplicatedRecordException.class)
    public ResponseEntity<GlobalRes<String>> duplicatedRecordHandle(DuplicatedRecordException e) {
        return ResponseEntity.status(409).body(
            GlobalRes.<String>builder()
                .code("E11")
                .message(e.getMessage())
                .build()
        );
    }

    @ExceptionHandler(CapacityExceededException.class)
    public ResponseEntity<GlobalRes<String>> capacityExceededHandle(CapacityExceededException e) {
        return ResponseEntity.status(400).body(
            GlobalRes.<String>builder()
                .code("E12")
                .message(e.getMessage())
                .build()
        );
    }

    @ExceptionHandler(ScheduleOverlapException.class)
    public ResponseEntity<GlobalRes<String>> scheduleOverlapHandle(ScheduleOverlapException e) {
        return ResponseEntity.status(400).body(
            GlobalRes.<String>builder()
                .code("E13")
                .message(e.getMessage())
                .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalRes<Map<String, String>>> methodArgumentNotValidHandle(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "유효하지 않은 값입니다.",
                (existing, replacement) -> existing
            ));

        return ResponseEntity.status(400).body(
            GlobalRes.<Map<String, String>>builder()
                .code("E21")
                .message("요청 파라미터에 이상이 있습니다.")
                .data(errors)
                .build()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalRes<String>> illegalArgumentHandle(IllegalArgumentException e) {
        log.warn("잘못된 파라미터 예외: {}", e.getMessage());
        return ResponseEntity.status(400).body(
            GlobalRes.<String>builder()
                .code("E22")
                .message("잘못된 요청 파라미터입니다.")
                .data(e.getMessage())
                .build()
        );
    }

    @ExceptionHandler(FileManagedException.class)
    public ResponseEntity<GlobalRes<String>> fileManagedHandle(FileManagedException e) {
        log.error("파일 처리 에러: {}", e.getMessage());
        return ResponseEntity.status(500).body(
            GlobalRes.<String>builder()
                .code("E40")
                .message("파일 처리 실패")
                .data(e.getMessage())
                .build()
        );
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<GlobalRes<String>> sqlHandle(SQLException e) {
        log.error("DB 에러", e);
        return ResponseEntity.status(500).body(
            GlobalRes.<String>builder()
                .code("E80")
                .message("DB 에러")
                .data("현재 서비스 이용이 불가합니다. 잠시후 다시 시도해 주십시오.")
                .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalRes<String>> othersHandle(Exception e) {
        log.error("시스템 에러", e);
        return ResponseEntity.status(500).body(
            GlobalRes.<String>builder()
                .code("E99")
                .message("시스템 에러")
                .data("현재 서비스 이용이 불가합니다. 잠시후 다시 시도해 주십시오.")
                .build()
        );
    }
}
