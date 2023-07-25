package com.inthefridges.api.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    // Common
    TEST_ERROR("C000",HttpStatus.BAD_REQUEST, "Test Error"),
    NOT_EXIST_API("C001", HttpStatus.BAD_REQUEST,  "요청 주소가 올바르지 않습니다."),
    METHOD_NOT_ALLOWED("C002", HttpStatus.METHOD_NOT_ALLOWED,  "사용할 수 없는 http 메서드입니다."),
    INTERNAL_SERVER_ERROR("C003", HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다."),
    NOT_FOUND("C004", HttpStatus.NOT_FOUND, "존재하지 않는 페이지입니다."),
    CONTENT_TYPE_NOT_SUPPORTED("C005", HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원되지 않는 미디어 타입입니다."),
    MISSING_DATA("C005", HttpStatus.BAD_REQUEST,  "요청에 필요한 데이터가 없습니다."),
    NOT_VALID("C006", HttpStatus.BAD_REQUEST, "올바르지 않은 요청입니다."),

    // Auth
    NOT_SUPPORTED_SOCIAL("A001", HttpStatus.NOT_FOUND, "지원하지 않는 소셜입니다."),
    EXPIRED_TOKEN("A002", HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN("A003", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");

    private String errorCode;
    private HttpStatus status;
    private String message;
}
