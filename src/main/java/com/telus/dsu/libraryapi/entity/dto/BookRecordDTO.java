package com.telus.dsu.libraryapi.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class BookRecordDTO {
    private Integer transaction;
    private Date tookOn;
    private Date returnOn;
    private Date dueDate;
    private Boolean isReturned;
    private Integer renewalCont;
    private Double delayPenalization;
    private String bookIsbn;
    private Integer userCode;
}
