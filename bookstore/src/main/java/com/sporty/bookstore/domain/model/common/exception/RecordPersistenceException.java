package com.sporty.bookstore.domain.model.common.exception;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 12:19 PM
 */
public class RecordPersistenceException extends RecordConflictException {

    public RecordPersistenceException(final String message) {
        super(message, ErrorCode.RECORD_CONFLICT);
    }
}
