package com.sporty.bookstore.controller.model.response.common;

import com.sporty.bookstore.domain.model.common.exception.ErrorCode;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 4:14 PM
 */
public record ErrorResponse(ErrorCode errorCode, String message) {
}
