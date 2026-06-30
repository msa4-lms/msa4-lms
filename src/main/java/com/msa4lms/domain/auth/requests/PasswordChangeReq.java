package com.msa4lms.domain.auth.requests;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordChangeReq(

        @NotBlank(message = "현재 비밀번호 입력은 필수입니다.")
        String currentPassword,

        @NotBlank(message = "비밀번호는 필수항목 입니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()])[A-Za-z\\d!@#$%^&*()]{8,20}$",
                message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자여야 합니다."
        )
        String newPassword,

        @NotBlank(message = "비밀번호 확인은 필수입니다.")
        String confirmPassword
) {

    @AssertTrue (message = "비밀번호와 비밀번호 확인이 일치하지 않습니다.")
    public boolean isPasswordMatch() {
        if(this.newPassword == null || this.confirmPassword == null) {
            return false;
        }

        return this.newPassword.equals(this.confirmPassword);
    }
}
