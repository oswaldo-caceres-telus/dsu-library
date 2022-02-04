package com.telus.dsu.libraryapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;
    @NotNull(message = "the ISBN is required")
    private String title;

    @Column(name = "isbn", updatable = false, unique = true)
    @NotNull(message = "the ISBN is required")
    private String isbn;
    @NotNull(message = "the ISBN is required")
    private String author;
    @NotNull(message = "the ISBN is required")
    private String category;
    @NotNull(message = "the ISBN is required")
    private Boolean isAvailable;

    @JsonIgnore
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookRecord> bookRecordList;

    public Book(Integer bookId) {
        this.bookId = bookId;
    }

    public Book() {
    }
}
