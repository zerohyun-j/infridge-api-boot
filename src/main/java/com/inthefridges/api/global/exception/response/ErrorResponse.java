package com.inthefridges.api.global.exception.response;

import com.inthefridges.api.global.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private String errorCode;
    private int status;
    private String message;
    private List<FieldError> fieldErrors;

    /**
     * http 상태 코드와 예외 메시지를 받는 생성자
     * ErrorResponse(400, "임시 에러", "EC_001")
     **/
    public ErrorResponse(String errorCode, int status, String message) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }

    /**
     * 요청 바디 필드 유효성 검증 예외 리스트를 받는 생성자
     **/
    private ErrorResponse(final List<FieldError> fieldErrors) {
        this.status = 400;
        this.fieldErrors = fieldErrors;
    }

    public static ErrorResponse of(ExceptionCode code) {
        // errorCode : 내부적으로 정의된 에러코드를 추가함
        return new ErrorResponse(code.getErrorCode(), code.getStatus().value(), code.getMessage());
    }

    /**
     * Validated 예외 에러 응답 메서드
     * */
    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(FieldError.of(bindingResult));
    }

    /**
     * 요청 바디 필드 유효성 검증 예외
     * */
    @Getter
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private Object rejectedValue;
        private String reason;

        public static List<FieldError> of(BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }


}
