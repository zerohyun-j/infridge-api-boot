package com.inthefridges.api.global.exception.response;

import com.inthefridges.api.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private String errorCode;
    private int status;
    private String message;

    /**
     * http 상태 코드와 예외 메시지를 받는 생성자 <br/>
     * ErrorResponse(400, "임시 에러", "EC_001")
     **/
    public ErrorResponse(String errorCode, int status, String message) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }

    public static ErrorResponse of(ExceptionCode code) {
        // errorCode : 내부적으로 정의된 에러코드를 추가함
        return new ErrorResponse(code.getErrorCode(), code.getStatus().value(), code.getMessage());
    }

}
