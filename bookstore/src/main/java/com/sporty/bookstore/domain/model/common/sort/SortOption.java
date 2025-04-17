package com.sporty.bookstore.domain.model.common.sort;

import lombok.Getter;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 12:51 PM
 */
@Getter
public enum SortOption {

    ASC("asc"),
    DESC("desc");

    private final String value;

    SortOption(final String value) {
        this.value = value;
    }
}
