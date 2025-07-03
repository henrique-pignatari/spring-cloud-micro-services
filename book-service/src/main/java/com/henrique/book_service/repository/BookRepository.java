package com.henrique.book_service.repository;

import com.henrique.book_service.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
