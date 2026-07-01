package com.msa4lms.global.errors.custom;

public class PasswordSameException extends RuntimeException {
    public PasswordSameException(String message) {
        super(message);
    }
}
