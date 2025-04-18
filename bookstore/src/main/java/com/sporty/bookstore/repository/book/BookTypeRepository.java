package com.sporty.bookstore.repository.book;

import com.sporty.bookstore.domain.entity.book.BookType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Created by Tigran Melkonyan
 * Date: 4/18/25
 * Time: 12:27 AM
 */
public interface BookTypeRepository extends JpaRepository<BookType, UUID> {
    
    boolean existsByName(String name);
}
