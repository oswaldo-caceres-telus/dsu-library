package com.telus.dsu.libraryapi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class BookRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;

    @Column(name = "TRANSACTION_NUMBER", unique = true)
    @NotNull(message = "number of transaction is required")
    private Integer transaction;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tookOn;
    private Date returnOn;
    private Date dueDate;
    private Boolean isReturned;
    private Integer renewalCont;
    private Double delayPenalization;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Book book;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public BookRecord() {
    }

    @PrePersist
    protected void onCreate() {this.tookOn = new Date();}

}
