package com.sporty.bookstore.controller.model.response.common;

import java.util.List;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 3:36 PM
 */
public record PageResponse<T>(List<T> items, long totalCount) {
}
