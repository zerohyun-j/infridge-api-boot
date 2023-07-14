package com.inthefridges.api.global.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException{
    private ExceptionCode exceptionCode;

    public ServiceException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
