package com.msa4lms.global.errors.custom;

public class DuplicatedRecordException extends RuntimeException {
    public DuplicatedRecordException(String message) {
        super(message);
    }
}
