package com.sporty.bookstore.domain.model.common.page;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 12:52 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableModel {

    @Min(0)
    private int page;

    @Min(1)
    private int size;

    public static PageableModel getPageRequestOrDefault(final PageableModel model) {
        if (Objects.isNull(model) || (model.getSize() < 1 || model.getPage() < 0)) {
            return new PageableModel(0, 50);
        }
        return new PageableModel(model.getPage() * model.getSize(), model.getSize());
    }
}
