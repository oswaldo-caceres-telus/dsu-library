package com.telus.dsu.libraryapi.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookDTO {
    private String title;
    private String isbn;
    private String author;
    private String category;
    private Boolean isAvailable;
}
