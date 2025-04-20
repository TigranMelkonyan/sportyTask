package com.sporty.iam.domain.model.common.exception;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 12:16 PM
 */
public class RecordNotFoundException extends RecordConflictException {

    public RecordNotFoundException(final String message) {
        super(message, ErrorCode.NOT_EXISTS_EXCEPTION);
    }
}
