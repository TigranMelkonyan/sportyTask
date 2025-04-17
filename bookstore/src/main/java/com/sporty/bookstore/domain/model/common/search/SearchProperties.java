package com.sporty.bookstore.domain.model.common.search;

import com.sporty.bookstore.domain.entity.common.ModelStatus;
import com.sporty.bookstore.domain.model.common.page.PageableModel;
import com.sporty.bookstore.domain.model.common.sort.SortOption;
import com.sporty.bookstore.domain.model.common.validate.ValidatableModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 12:50 PM
 */
@Getter
@Setter
public class SearchProperties implements ValidatableModel {

    @Size(max = 100, message = "Search text cannot be longer than 100 characters")
    private String searchText;

    @NotNull(message = "status required")
    private ModelStatus status;

    private SortOption sort;

    @Valid
    private PageableModel pageable;

}
