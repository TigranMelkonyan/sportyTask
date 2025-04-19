package com.sporty.bookstore.controller.model.mapper.search;

import com.sporty.bookstore.controller.model.request.common.search.SearchPropertiesRequest;
import com.sporty.bookstore.domain.model.common.page.PageableModel;
import com.sporty.bookstore.domain.model.common.search.SearchProperties;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 3:42 PM
 */
@Mapper(componentModel = "spring")
public interface SearchPropertiesRequestMapper {

    default SearchProperties toSearchProperties(SearchPropertiesRequest request, int page, int size) {
        SearchProperties properties = mapRequestProperties(request);
        properties.setPageable(createPageableModel(page, size));
        return properties;
    }

    @Mapping(target = "pageable", ignore = true)
    SearchProperties mapRequestProperties(SearchPropertiesRequest request);

    default PageableModel createPageableModel(int page, int size) {
        PageableModel pageable = new PageableModel();
        pageable.setPage(page);
        pageable.setSize(size);
        return pageable;
    }
}
