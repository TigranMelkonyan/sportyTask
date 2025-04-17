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

    default SearchProperties toSearchProperties(SearchPropertiesRequest request, String page, String size) {
        SearchProperties properties = mapRequestProperties(request);
        properties.setPageable(createPageableModel(page, size));
        return properties;
    }

    @Mapping(target = "pageable", ignore = true)
    SearchProperties mapRequestProperties(SearchPropertiesRequest request);

    default PageableModel createPageableModel(String page, String size) {
        PageableModel pageable = new PageableModel();
        pageable.setPage(Integer.parseInt(page));
        pageable.setSize(Integer.parseInt(size));
        return pageable;
    }
}
