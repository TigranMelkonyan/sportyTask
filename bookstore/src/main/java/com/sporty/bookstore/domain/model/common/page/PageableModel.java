package com.sporty.bookstore.domain.model.common.page;

import com.sporty.bookstore.domain.model.common.validate.ValidatableModel;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 12:52 PM
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageableModel implements ValidatableModel {

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
