package com.sporty.bookstore.repository.book;

import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.common.search.SearchProperties;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 1:02 PM
 */
public interface BookRepositoryCustom {

    PageModel<Book> search(final SearchProperties searchProperties);

}
