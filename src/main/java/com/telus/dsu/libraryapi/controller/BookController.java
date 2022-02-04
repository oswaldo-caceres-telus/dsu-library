package com.telus.dsu.libraryapi.controller;

import com.telus.dsu.libraryapi.entity.Book;
import com.telus.dsu.libraryapi.entity.dto.BookDTO;
import com.telus.dsu.libraryapi.exception.ResourceNotCreatedException;
import com.telus.dsu.libraryapi.exception.ResourceNotFoundException;
import com.telus.dsu.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("")
    public ResponseEntity<?> createNewBook(@Valid @RequestBody Book book, BindingResult result){
        if(result.hasErrors()){
            throw new ResourceNotCreatedException("The book was not created");
        }else{
            Book newBook = bookService.createBook(book);
            return new ResponseEntity<BookDTO>(convertToDTO(newBook), HttpStatus.CREATED);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllBooks(){
        List<Book> books = bookService.getBooks();
        return new ResponseEntity<List<BookDTO>>(convertToListDTO(books), HttpStatus.OK);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<?> getBookByIsbn(@PathVariable String isbn){
        Book bookFound = bookService.getBookByIsbn(isbn);
        if(bookFound == null){
            throw new ResourceNotFoundException("book not found with id: "+isbn);
        }else{
            BookDTO bookDTO = convertToDTO(bookFound);
            return new ResponseEntity<BookDTO>(bookDTO, HttpStatus.OK);
        }
    }
    @PutMapping("/{isbn}")
    public ResponseEntity<?> updateBook(@Valid @RequestBody Book book, BindingResult result, @PathVariable String isbn ){
        Book bookToUpdate = bookService.getBookByIsbn(isbn);

        if(result.hasErrors() || bookToUpdate == null){
            throw new ResourceNotFoundException("book not found with id: "+isbn);
        }else{
            Book updated = bookService.updateBook(bookToUpdate, book);
            return new ResponseEntity<>(convertToDTO(updated), HttpStatus.OK);
        }
    }
    @DeleteMapping("/{isbn}")
    public ResponseEntity<?> deleteBook(@PathVariable String isbn){
        bookService.deleteBook(isbn);
        return new ResponseEntity<>("Book with ISBN "+ isbn + "deleted", HttpStatus.OK);
    }


    private List<BookDTO> convertToListDTO(List<Book> books){
        List<BookDTO> bookDTOList = new ArrayList<>();
        for(Book book : books){
            bookDTOList.add(convertToDTO(book));
        }
        return bookDTOList;
    }

    private BookDTO convertToDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

}
