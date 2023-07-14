package com.inthefridges.api.global.exception;

import com.inthefridges.api.global.exception.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String EXCEPTION_FORMAT = "[EXCEPTION]                   -----> ";
    private static final String EXCEPTION_MESSAGE_FORMAT = "[EXCEPTION] EXCEPTION_MESSAGE -----> [{}]";
    private static final String EXCEPTION_TYPE_FORMAT = "[EXCEPTION] EXCEPTION_TYPE    -----> [{}]";
    private static final String EXCEPTION_REQUEST_URI = "[EXCEPTION] REQUEST_URI       -----> [{}]";
    private static final String EXCEPTION_HTTP_METHOD_TYPE = "[EXCEPTION] HTTP_METHOD_TYPE  -----> [{}]";

    /** 서버 커스텀 서비스 예외 처리 */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException e) {
        ExceptionCode code = e.getExceptionCode();
        return ResponseEntity
                .status(code.getStatus())
                .body(ErrorResponse.of(code));
    }

    /** @Valid 예외 처리 */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(
            HttpServletRequest request,
            BindException e
    ) {
        logDebug(request, e);
        return ErrorResponse.of(ExceptionCode.NOT_VALID);
    }

    /** @Validated 예외 처리 */
//    @ResponseStatus(BAD_REQUEST)
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ErrorResponse handleConstraintViolationException(
//            HttpServletRequest request,
//            ConstraintViolationException e
//    ) {
//        logDebug(request, e);
//        return ErrorResponse.of(ExceptionCode.NOT_VALID);
//    }

    /** 쿼리 파라미터가 결여(잘못된 타입 등) 됐을 때 예외 처리 */
    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(
            HttpServletRequest request,
            MissingServletRequestParameterException e) {
        logDebug(request, e);
        return ErrorResponse.of(ExceptionCode.NOT_VALID);
    }

    /** 해당 api(uri)에 잘못된 HttpMethod 요청 예외 처리 */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(
            HttpServletRequest request,
            HttpRequestMethodNotSupportedException e) {
        logDebug(request, e);
        return ErrorResponse.of(ExceptionCode.METHOD_NOT_ALLOWED);
    }

    /** HTTP message body 없이 요청 예외 처리 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e
    ) {
        logError(e);
        return ErrorResponse.of(ExceptionCode.MISSING_DATA);
    }

    /** 없는 api(uri)에 대한 요청 예외처리
     *  TODO: 스프링 white page, 톰캣 404 페이지 처리
     * */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({NoHandlerFoundException.class})
    public ErrorResponse handleNoHandlerFoundException(HttpServletRequest request, NoHandlerFoundException e) {
        logWarn(request, e);
        return ErrorResponse.of(ExceptionCode.NOT_EXIST_API);
    }

    /**
     * 인증 예외 처리 TODO : spring security 적용 시 주석 해제
     * */
//    @ResponseStatus(UNAUTHORIZED)
//    @ExceptionHandler({ AuthenticationException.class })
//    public ErrorResponse handleAuthenticationExceptionException(Exception e) {
//        logError(e);
//        return ErrorResponse.of(ExceptionCode.UNAUTHORIZED);
//    }

    /**
     * 인가 예외 처리 TODO : spring security 적용 시 주석 해제
     * */
//    @ResponseStatus(FORBIDDEN)
//    @ExceptionHandler({ AccessDeniedException.class })
//    public ErrorResponse handleAccessDeniedException(
//            Exception e) {
//        logError(e);
//        return ErrorResponse.of(ExceptionCode.FORBIDDEN_MEMBER);
//    }

    /**
     * 예상하지 못한 서버 예외 처리
     */
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ErrorResponse handleServerException(HttpServletRequest request, Exception e) {
        logError(e);

        if (e instanceof MethodArgumentTypeMismatchException) {
            return ErrorResponse.of(ExceptionCode.NOT_EXIST_API);
        }

        return ErrorResponse.of(ExceptionCode.INTERNAL_SERVER_ERROR);
    }


    /** 심각한 오류나 예외 상황을 로깅 */
    private void logError(Exception e) {
        log.error(EXCEPTION_TYPE_FORMAT, e.getClass().getSimpleName());
        log.error(EXCEPTION_MESSAGE_FORMAT, e.getMessage());
        log.error(EXCEPTION_FORMAT, e);
    }

    /** 잠재적으로 문제가 될 수 있는 상황 로깅 */
    private void logWarn(HttpServletRequest request, Exception e) {
        log.warn(EXCEPTION_REQUEST_URI, request.getRequestURI());
        log.warn(EXCEPTION_HTTP_METHOD_TYPE, request.getMethod());
        log.warn(EXCEPTION_TYPE_FORMAT, e.getClass().getSimpleName());
        log.warn(EXCEPTION_MESSAGE_FORMAT, e.getMessage());
    }

    /** 개발 혹은 테스트 단계에서 해당 기능들이 올바르게 작동하는지 확인하기 위한 로깅 */
    private void logDebug(HttpServletRequest request, Exception e) {
        log.debug(EXCEPTION_REQUEST_URI, request.getRequestURI());
        log.debug(EXCEPTION_HTTP_METHOD_TYPE, request.getMethod());
        log.debug(EXCEPTION_TYPE_FORMAT, e.getClass().getSimpleName());
        log.debug(EXCEPTION_MESSAGE_FORMAT, e.getMessage());
    }
}
