package com.sporty.bookstore.controller.model.request.common.search;

import com.sporty.bookstore.domain.entity.common.ModelStatus;
import com.sporty.bookstore.domain.model.common.sort.SortOption;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 3:39 PM
 */
@Getter
@Setter
public class SearchPropertiesRequest {

    @Size(max = 100, message = "Search text cannot be longer than 100 characters")
    private String searchText;

    @NotNull(message = "status required")
    private ModelStatus status;

    private SortOption sort;

}
