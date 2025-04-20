package com.sporty.iam.domain.model.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Tigran Melkonyan
 * Date: 2/18/25
 * Time: 9:09 PM
 */
@RequiredArgsConstructor
@Getter
public class RecordConflictException extends RuntimeException {

    private final String message;
    private final ErrorCode errorCode;
}
