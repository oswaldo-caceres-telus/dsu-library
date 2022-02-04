package com.telus.dsu.libraryapi.service;

import com.telus.dsu.libraryapi.entity.Book;
import com.telus.dsu.libraryapi.exception.ResourceNotCreatedException;
import com.telus.dsu.libraryapi.exception.ResourceNotFoundException;
import com.telus.dsu.libraryapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getBooks(){
        return bookRepository.findAll();
    }

    public Book createBook(Book book){
        try{
            return bookRepository.save(book);
        }catch (Exception e){
            throw new ResourceNotCreatedException("Book with ISBN: "+book.getIsbn()+" already exist");
        }

    }
    public Book getBookByIsbn(String isbn){
        return bookRepository.findBookByIsbn(isbn);
    }

    public Book updateBook(Book toUpdateBook, Book book){
        toUpdateBook.setTitle(book.getTitle());
        toUpdateBook.setIsAvailable(book.getIsAvailable());
        toUpdateBook.setCategory(book.getCategory());
        toUpdateBook.setAuthor(book.getAuthor());

        return bookRepository.save(toUpdateBook);
    }
    public void deleteBook(String isbn){
        Book book = bookRepository.findBookByIsbn(isbn);
        if(book == null){
            throw new ResourceNotFoundException("Book not found with isbn: "+isbn);
        }else {
            bookRepository.delete(book);
        }
    }
}
