package com.sporty.bookstore.domain.model.common.exception;

import lombok.Getter;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 12:14 PM
 */
@Getter
public class RecordConflictException extends RuntimeException {

    private final ErrorCode errorCode;

    public RecordConflictException(final String message, final ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
