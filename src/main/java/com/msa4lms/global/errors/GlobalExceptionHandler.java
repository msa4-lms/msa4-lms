package com.msa4lms.global.errors;

import com.msa4lms.global.responses.GlobalRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.validation.FieldError;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalRes<Map<String, String>>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid value",
                (existing, replacement) -> existing
            ));

        return ResponseEntity.status(400).body(
            GlobalRes.<Map<String, String>>builder()
                .code("E20")
                .message("입력값이 유효하지 않습니다.")
                .data(errors)
                .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalRes<String>> handleAll(Exception e) {
        log.error("시스템 에러", e);
        return ResponseEntity.status(500).body(
            GlobalRes.<String>builder()
                .code("E99")
                .message("서버 내부 에러가 발생했습니다.")
                .data(e.getMessage())
                .build()
        );
    }
}
