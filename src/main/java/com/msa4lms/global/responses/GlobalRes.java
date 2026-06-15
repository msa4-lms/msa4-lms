package com.msa4lms.global.responses;

/**
 * 모든 API 응답의 공통 규격을 정의하는 Record입니다.
 * GEMINI.md 컨벤션(Part 2-6)에 따라 code, message, data 구조를 유지합니다.
 */
public record GlobalRes<T>(
    String code,    // 응답 코드 (성공: "00", 에러: "E01" 등)
    String message, // 응답 메시지
    T data          // 실제 응답 데이터
) {
    /**
     * 성공 응답을 생성하는 정적 팩토리 메서드입니다.
     * 
     * @param data 결과 데이터
     * @return GlobalRes 객체
     */
    public static <T> GlobalRes<T> success(T data) {
        return new GlobalRes<>("00", "정상 처리되었습니다.", data);
    }

    /**
     * 에러 응답을 생성하는 정적 팩토리 메서드입니다.
     * 
     * @param code 에러 코드
     * @param message 에러 메시지
     * @param data 추가적인 에러 정보 (필요시)
     * @return GlobalRes 객체
     */
    public static <T> GlobalRes<T> error(String code, String message, T data) {
        return new GlobalRes<>(code, message, data);
    }
}
