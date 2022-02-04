package com.telus.dsu.libraryapi.service;

import com.telus.dsu.libraryapi.entity.Book;
import com.telus.dsu.libraryapi.entity.BookRecord;
import com.telus.dsu.libraryapi.entity.User;
import com.telus.dsu.libraryapi.exception.ResourceNotCreatedException;
import com.telus.dsu.libraryapi.exception.ResourceNotFoundException;
import com.telus.dsu.libraryapi.repository.BookRecordRepository;
import com.telus.dsu.libraryapi.repository.BookRepository;
import com.telus.dsu.libraryapi.repository.UserRepository;
import com.telus.dsu.libraryapi.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class BookRecordService {

    @Autowired
    private BookRecordRepository bookRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BookRecord> getBookRecords() {
        return bookRecordRepository.findAll();
    }

    public BookRecord getBookRecordByTransaction(Integer transaction) {
        return bookRecordRepository.findBookRecordByTransaction(transaction);
    }

    public BookRecord createBookRecord(BookRecord bookRecord, String isbn, Integer userCode) {
        Book book = bookRepository.findBookByIsbn(isbn);
        User user = userRepository.findByUserCode(userCode);
        if (book == null) {
            throw new ResourceNotFoundException("Book with ISBN " + isbn + " does not exist");
        } else if (user == null || !user.getIsActive()) {
            throw new ResourceNotFoundException("User with code #" + userCode + " does not exist or is deactivated");
        } else if (user.getBorrowedBooks() >= Constants.MAX_BOOKS_PER_USER) {
            throw new ResourceNotCreatedException("User has already borrow 3 books");
        } else if (!book.getIsAvailable()) {
            throw new ResourceNotCreatedException("Book is not available");
        }

        Date currentDataPlusSeven = sumSevenDays(new Date());

        bookRecord.setDueDate(currentDataPlusSeven);
        bookRecord.setRenewalCont(0);
        bookRecord.setIsReturned(false);
        bookRecord.setUser(user);
        bookRecord.setBook(book);
        book.setIsAvailable(false);
        user.setBorrowedBooks(user.getBorrowedBooks() + 1);
        try {
            return bookRecordRepository.save(bookRecord);
        } catch (Exception e) {
            throw new ResourceNotCreatedException("Invoice #" + bookRecord.getTransaction() + " already exists");
        }
    }

    public BookRecord returnBook(Integer invoice, String isbn, Integer userCode) {
        BookRecord bookRecord = bookRecordRepository.findBookRecordByTransaction(invoice);
        Book book = bookRepository.findBookByIsbn(isbn);
        User user = userRepository.findByUserCode(userCode);

        validateEntries(bookRecord, book, user, invoice, isbn, userCode);
        user.setBorrowedBooks(user.getBorrowedBooks() - 1);
        book.setIsAvailable(true);
        bookRecord.setIsReturned(true);
        bookRecord.setReturnOn(new Date());

        Date tookOn = bookRecord.getTookOn();
        Date dueDate = bookRecord.getDueDate();
        Date returnOn = new Date();

        Long difference = getDifferenceBetweenDays(tookOn, returnOn);
        if (difference > 7) {
            Long diffForPenalization = getDifferenceBetweenDays(dueDate, returnOn);
            bookRecord.setDelayPenalization((diffForPenalization) * Constants.PENALIZATION);
            bookRecordRepository.save(bookRecord);
            throw new ResourceNotCreatedException("User: "+userCode+" has delay of "+diffForPenalization+"days, penalization applied");
        }
        return bookRecordRepository.save(bookRecord);
    }

    public BookRecord renewBook(Integer invoice, String isbn, Integer userCode) {
        BookRecord bookRecord = bookRecordRepository.findBookRecordByTransaction(invoice);
        Book book = bookRepository.findBookByIsbn(isbn);
        User user = userRepository.findByUserCode(userCode);

        validateEntries(bookRecord, book, user, invoice, isbn, userCode);

        Date dueDateUpdated = sumSevenDays(new Date());

        Date tookOn = bookRecord.getTookOn();
        Date dueDate = bookRecord.getDueDate();
        Date renewOn = new Date();
        Long differenceBefore = getDifferenceBetweenDays(tookOn, renewOn);

        if (differenceBefore > 7) {
            Long penalization = getDifferenceBetweenDays(dueDate, renewOn);
            book.setIsAvailable(true);
            user.setBorrowedBooks(user.getBorrowedBooks() - 1);
            bookRecord.setIsReturned(true);
            bookRecord.setReturnOn(renewOn);
            bookRecord.setDelayPenalization((penalization) * Constants.PENALIZATION);
            bookRecordRepository.save(bookRecord);
            throw new ResourceNotCreatedException("The user has delay, the book: "+book.getTitle()+" was returned");

        }

        bookRecord.setTookOn(renewOn);
        bookRecord.setDueDate(dueDateUpdated);
        bookRecord.setRenewalCont(bookRecord.getRenewalCont() + 1);

        if (bookRecord.getRenewalCont() > Constants.MAX_RENEWALS) {
            bookRecord.setReturnOn(renewOn);
            bookRecord.setIsReturned(true);
            book.setIsAvailable(true);
            user.setBorrowedBooks(user.getBorrowedBooks() - 1);
            bookRecord.setRenewalCont(bookRecord.getRenewalCont() - 1);
            bookRecordRepository.save(bookRecord);
            throw new ResourceNotCreatedException("User " + userCode + "has reach maximum renewals for the book: " + book.getTitle());
        }

        return bookRecordRepository.save(bookRecord);
    }

    public BookRecord updateBookRecord(BookRecord bookRecordToUpdate, BookRecord bookRecord) {
        bookRecordToUpdate.setTookOn(bookRecord.getTookOn());
        bookRecordToUpdate.setReturnOn(bookRecord.getReturnOn());
        bookRecordToUpdate.setDueDate(bookRecord.getDueDate());
        bookRecordToUpdate.setIsReturned(bookRecord.getIsReturned());
        bookRecordToUpdate.setRenewalCont(bookRecord.getRenewalCont());
        bookRecordToUpdate.setDelayPenalization(bookRecord.getDelayPenalization());

        return bookRecordRepository.save(bookRecordToUpdate);
    }

    public void deleteBookRecord(Integer transaction) {
        BookRecord bookRecordFound = bookRecordRepository.findBookRecordByTransaction(transaction);

        if (bookRecordFound == null) {
            throw new ResourceNotFoundException("BookRecord not found with BookRecordId: " + transaction);
        } else {
            User user = userRepository.findByUserId(bookRecordFound.getUser().getUserId());
            user.setBorrowedBooks(user.getBorrowedBooks() - 1);
            Book book = bookRepository.findByBookId(bookRecordFound.getBook().getBookId());
            book.setIsAvailable(true);
            bookRecordRepository.delete(bookRecordFound);
        }
    }

    public Long getDifferenceBetweenDays(Date date1, Date date2) {
        long days = date2.getTime() - date1.getTime();
        return TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS);
    }

    public void validateEntries(BookRecord bookRecord, Book book, User user, Integer invoice, String isbn, Integer userCode) {
        if (bookRecord == null) {
            throw new ResourceNotCreatedException("The invoice #" + invoice + " does not exist");
        } else if (book == null) {
            throw new ResourceNotFoundException("The book with ISBN: " + isbn + " does not exists");
        } else if (user == null || !user.getIsActive()) {
            throw new ResourceNotFoundException("The user with id: " + userCode + " does not exists or is deactivated");
        } else if (bookRecord.getIsReturned()) {
            throw new ResourceNotCreatedException("The book: " + book.getTitle() + " has been already returned");
        } else if (!bookRecord.getBook().getBookId().equals(book.getBookId())) {
            throw new ResourceNotCreatedException("This book does not exist in the current invoice #" + invoice);
        } else if (!bookRecord.getUser().getUserId().equals(user.getUserId())) {
            throw new ResourceNotCreatedException("This user does not exist in the current invoice #" + invoice);
        }
    }

    public Date sumSevenDays(Date today) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat.format(today);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, 7);
        return calendar.getTime();
    }

}
