package com.msa4lms.domain.auth.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginReq(
        @NotBlank(message = "학번은 필수입니다.")
        @Pattern(regexp = "^\\d{10}$")
        String userNo,

        @NotBlank(message = "비밀번호는 필수입니다")
        @Pattern(regexp = "^[0-9a-zA-Z!@#$%^&*()]{8,20}$", message = "허용하지 않는 양식입니다.")
        String password
) {
}
