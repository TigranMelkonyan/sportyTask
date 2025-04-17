package com.sporty.bookstore.repository.book.impl;

import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.model.common.page.PageModel;
import com.sporty.bookstore.domain.model.common.page.PageableModel;
import com.sporty.bookstore.domain.model.common.search.SearchProperties;
import com.sporty.bookstore.repository.book.BookRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
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
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> bookRoot = cq.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(searchProperties.getSearchText())) {
            Predicate bookTitlePredicate = cb.like(bookRoot.get("title"), "%"
                    + searchProperties.getSearchText() + "%");
            Predicate authorPredicate = cb.like(bookRoot.get("author"), "%"
                    + searchProperties.getSearchText() + "%");
            predicates.add(cb.or(bookTitlePredicate, authorPredicate));
        }

        if (Objects.nonNull(searchProperties.getStatus())) {
            predicates.add(cb.equal(bookRoot.get("status"), searchProperties.getStatus()));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        if (Objects.nonNull(searchProperties.getSort())) {
            if ("desc".equalsIgnoreCase(searchProperties.getSort().getValue())) {
                cq.orderBy(cb.desc(bookRoot.get("createdon")));
            } else {
                cq.orderBy(cb.asc(bookRoot.get("createdon")));
            }
        }

        TypedQuery<Book> query = entityManager.createQuery(cq);
        PageableModel pageRequest = PageableModel.getPageRequestOrDefault(searchProperties.getPageable());
        query.setFirstResult(pageRequest.getPage() * pageRequest.getSize());
        query.setMaxResults(pageRequest.getSize());

        List<Book> books = query.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Book> countRoot = countQuery.from(Book.class);
        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageModel<>(books, count);
    }
}
