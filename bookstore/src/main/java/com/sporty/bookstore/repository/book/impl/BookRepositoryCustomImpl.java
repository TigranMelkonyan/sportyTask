package com.sporty.bookstore.repository.book.impl;

import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.common.page.PageableModel;
import com.sporty.bookstore.domain.model.common.search.SearchProperties;
import com.sporty.bookstore.repository.book.BookRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 1:03 PM
 */
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PageModel<Book> search(final SearchProperties searchProperties) {
        StringBuilder jpql = new StringBuilder("SELECT b FROM Book b WHERE 1=1");
        StringBuilder countJpql = new StringBuilder("SELECT COUNT(b) FROM Book b WHERE 1=1");

        Map<String, Object> params = new HashMap<>();

        if (StringUtils.hasText(searchProperties.getSearchText())) {
            jpql.append(" AND (LOWER(b.title) LIKE :searchText OR LOWER(b.author) LIKE :searchText)");
            countJpql.append(" AND (LOWER(b.title) LIKE :searchText OR LOWER(b.author) LIKE :searchText)");
            params.put("searchText", "%" + searchProperties.getSearchText().toLowerCase() + "%");
        }

        if (Objects.nonNull(searchProperties.getStatus())) {
            jpql.append(" AND b.status = :status");
            countJpql.append(" AND b.status = :status");
            params.put("status", searchProperties.getStatus());
        }

        if (searchProperties.getSort() != null) {
            String direction = "asc".equalsIgnoreCase(searchProperties.getSort().getValue()) ? "ASC" : "DESC";
            jpql.append(" ORDER BY b.createdOn ").append(direction);
        }

        TypedQuery<Book> query = entityManager.createQuery(jpql.toString(), Book.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql.toString(), Long.class);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        PageableModel pageRequest = PageableModel.getPageRequestOrDefault(searchProperties.getPageable());
        query.setFirstResult(pageRequest.getPage() * pageRequest.getSize());
        query.setMaxResults(pageRequest.getSize());

        List<Book> books = query.getResultList();
        Long count = countQuery.getSingleResult();

        return new PageModel<>(books, count);
    }

}
