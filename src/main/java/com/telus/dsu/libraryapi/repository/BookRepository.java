package com.telus.dsu.libraryapi.repository;

import com.telus.dsu.libraryapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Book findBookByIsbn(String isbn);
    Book findByBookId(Integer id);
}
