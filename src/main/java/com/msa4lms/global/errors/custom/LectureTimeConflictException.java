package com.msa4lms.global.errors.custom;

/**
 * 교수가 강의를 개설할 때, 본인이 같은 학기에 이미 담당 중인 다른 강의와
 * 시간표(요일·교시)가 겹치는 경우 발생시키는 예외.
 */
public class LectureTimeConflictException extends RuntimeException {
    public LectureTimeConflictException(String message) {
        super(message);
    }
}
