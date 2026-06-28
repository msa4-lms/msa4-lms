package com.msa4lms.domain.profile.controllers;

import com.msa4lms.domain.profile.responses.StudentProfileRes;
import com.msa4lms.domain.profile.services.StudentService;
import com.msa4lms.global.annotations.LoginUserId;
import com.msa4lms.global.responses.GlobalRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/profile")
    public ResponseEntity<GlobalRes<StudentProfileRes>> getProfile(
            @LoginUserId Long userId
            ){
        StudentProfileRes profile =
                studentService.getProfile(userId);

        return ResponseEntity.ok(
                GlobalRes.<StudentProfileRes>builder()
                        .code("00")
                        .message("프로필 조회 성공")
                        .data(profile)
                        .build()
        );
    }
}
