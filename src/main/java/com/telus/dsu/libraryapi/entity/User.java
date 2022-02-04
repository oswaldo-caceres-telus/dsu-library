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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(name = "USER_CODE", unique = true, updatable = false)
    @NotNull(message = "the UserCode is required")
    private Integer userCode;
    @NotNull(message = "The first name is required")
    private String firstName;
    @NotNull(message = "The last name is required")
    private String lastName;
    @NotNull(message = "the books is required")
    private Integer borrowedBooks;
    @NotNull(message = "the email is required")
    private String email;
    @NotNull(message = "the phone is required")
    private String phone;
    @NotNull(message = "the isActive is required")
    private Boolean isActive;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USERTYPE_ID")
    private UserType userType;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BookRecord> bookRecordList;

    public User(Integer userId) {
        this.userId = userId;
    }

    public User() {

    }
}
