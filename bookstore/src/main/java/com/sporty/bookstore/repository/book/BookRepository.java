package com.sporty.bookstore.repository.book;

import com.sporty.bookstore.domain.entity.book.Book;
import com.sporty.bookstore.domain.entity.common.ModelStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 11:25 AM
 */
public interface BookRepository extends JpaRepository<Book, UUID>, BookRepositoryCustom {

    Optional<Book> findByIdAndStatus(UUID id, ModelStatus status);
    
    boolean existsByAuthorAndTitle(String author, String title);
}
