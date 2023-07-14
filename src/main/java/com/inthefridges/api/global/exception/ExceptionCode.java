package com.inthefridges.api.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    // common
    TEST_ERROR("EC_000",HttpStatus.BAD_REQUEST, "Test Error"),
    NOT_EXIST_API("EC_001", HttpStatus.BAD_REQUEST,  "요청 주소가 올바르지 않습니다."),
    METHOD_NOT_ALLOWED("EC_002", HttpStatus.METHOD_NOT_ALLOWED,  "사용할 수 없는 http 메서드입니다."),
    INTERNAL_SERVER_ERROR("EC_003", HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다."),
    NOT_FOUND("EC_004", HttpStatus.NOT_FOUND, "존재하지 않는 페이지입니다."),
    CONTENT_TYPE_NOT_SUPPORTED("EC_005", HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원되지 않는 미디어 타입입니다."),
    MISSING_DATA("EC_005", HttpStatus.BAD_REQUEST,  "요청에 필요한 데이터가 없습니다."),
    NOT_VALID("EC_006", HttpStatus.BAD_REQUEST, "올바르지 않은 요청입니다.");

    private String errorCode;
    private HttpStatus status;
    private String message;
}
