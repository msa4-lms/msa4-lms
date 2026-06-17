package com.msa4lms.global.errors.custom;

public class PasswordChangeFailedException extends RuntimeException {
    public PasswordChangeFailedException(String message) {
        super(message);
    }
}
