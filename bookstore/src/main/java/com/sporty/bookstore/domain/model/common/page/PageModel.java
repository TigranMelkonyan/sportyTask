package com.sporty.bookstore.domain.model.common.page;

import java.util.List;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 12:49 PM
 */
public record PageModel<T>(List<T> items, long totalCount) {
    public PageModel {
        items = List.copyOf(items);
    }
}

