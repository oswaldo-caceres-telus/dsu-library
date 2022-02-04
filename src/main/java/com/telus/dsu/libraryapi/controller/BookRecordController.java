package com.telus.dsu.libraryapi.controller;

import com.telus.dsu.libraryapi.entity.BookRecord;
import com.telus.dsu.libraryapi.entity.User;
import com.telus.dsu.libraryapi.entity.dto.BookRecordDTO;
import com.telus.dsu.libraryapi.entity.dto.UserDTO;
import com.telus.dsu.libraryapi.exception.ResourceNotCreatedException;
import com.telus.dsu.libraryapi.exception.ResourceNotFoundException;
import com.telus.dsu.libraryapi.service.BookRecordService;
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
@RequestMapping("/api/bookRecord")
public class BookRecordController {
    @Autowired
    private BookRecordService bookRecordService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<?> getAllBookRecords(){
        List<BookRecord> bookRecords = bookRecordService.getBookRecords();
        return new ResponseEntity<List<BookRecordDTO>>(convertListToDTO(bookRecords), HttpStatus.OK);
    }

    @GetMapping("/{transaction}")
    public ResponseEntity<?> getBookRecordByTransaction(@PathVariable Integer transaction){
        BookRecord bookRecordFound = bookRecordService.getBookRecordByTransaction(transaction);
        if(bookRecordFound == null){
            throw new ResourceNotFoundException("BookRecord not found with Transaction: " + transaction);
        }else{
            BookRecordDTO bookRecordDTO = convertToDTO(bookRecordFound);
            return new ResponseEntity<BookRecordDTO>(bookRecordDTO, HttpStatus.OK);
        }
    }

    @PostMapping("/{isbn}/{userCode}")
    public ResponseEntity<?> createNewBookRecord(@Valid @RequestBody BookRecord bookRecord,
                                                 @PathVariable String isbn,
                                                 @PathVariable Integer userCode,
                                                 BindingResult result){
        if(result.hasErrors()){
            throw new ResourceNotCreatedException("BookRecord was not created");
        }else{
            BookRecord newBookRecord = bookRecordService.createBookRecord(bookRecord,isbn,userCode);
            return new ResponseEntity<BookRecordDTO>(convertToDTO(newBookRecord), HttpStatus.OK);
        }
    }

    @PostMapping("/return/{invoice}/{isbn}/{userCode}")
    public ResponseEntity<?> returnBook (@PathVariable Integer invoice,
                                         @PathVariable String isbn,
                                         @PathVariable Integer userCode){
        BookRecord record = bookRecordService.returnBook(invoice,isbn,userCode);
        return new ResponseEntity<>(convertToDTO(record), HttpStatus.OK);
    }

    @PostMapping("/renew/{invoice}/{isbn}/{userCode}")
    public ResponseEntity<?> renewBook(@PathVariable Integer invoice,
                                       @PathVariable String isbn,
                                       @PathVariable Integer userCode){
        BookRecord record = bookRecordService.renewBook(invoice,isbn,userCode);
        return new ResponseEntity<>(convertToDTO(record),HttpStatus.OK);
    }

    @PutMapping("/{transaction}")
    public ResponseEntity<?> updateBookRecord(@Valid @RequestBody BookRecord bookRecord, BindingResult result, @PathVariable Integer transaction){
        BookRecord bookRecordToUpdate = bookRecordService.getBookRecordByTransaction(transaction);
        if(result.hasErrors() || bookRecordToUpdate == null){
            throw new ResourceNotFoundException("Book Record not found with User Code: " + transaction);
        }else{
            BookRecord updatedBookRecord = bookRecordService.updateBookRecord(bookRecordToUpdate, bookRecord);
            return new ResponseEntity<BookRecordDTO>(convertToDTO(updatedBookRecord), HttpStatus.OK);
        }
    }

    @DeleteMapping("{transaction}")
    public ResponseEntity<?> deleteBookRecord(@PathVariable Integer transaction){
        bookRecordService.deleteBookRecord(transaction);
        return new ResponseEntity<>("BookRecord with transaction " + transaction + " deleted", HttpStatus.OK);
    }

    private List<BookRecordDTO> convertListToDTO(List<BookRecord> bookRecords){
        List<BookRecordDTO> bookRecordDTOList = new ArrayList<>();
        for(BookRecord bookRecord : bookRecords){
            bookRecordDTOList.add(convertToDTO(bookRecord));
        }
        return bookRecordDTOList;
    }

    private BookRecordDTO convertToDTO(BookRecord bookRecord){
        return modelMapper.map(bookRecord, BookRecordDTO.class);
    }

    private BookRecord convertToEntity(BookRecordDTO bookRecordDTO){
        return modelMapper.map(bookRecordDTO, BookRecord.class);
    }
}
