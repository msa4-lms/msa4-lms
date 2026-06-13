package com.example.msa4lms.global.errors;


import com.example.msa4lms.global.responses.GlobalRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClient;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final RestClient.Builder builder;

    public GlobalExceptionHandler(RestClient.Builder builder) {
        this.builder = builder;
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalRes<String>> otherHandle(Exception e) {
        log.error("시스템 에러: ", e);

        return ResponseEntity.status(500).body(
                GlobalRes.<String>builder()
                        .code("E99")
                        .message("시스템 에러")
                        .message("현재 서비스 이용이 불가합니다. 잠시후 다시 시도해 주십시오.")
                        .build()
        );
    }

}
