package com.telus.dsu.libraryapi.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USERTYPE_ID")
    private Integer userTypeId;

    @Column(name = "USER_TYPE", unique = true)
    @NotNull(message = "the UserType is required")
    private String userType;

    public UserType(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public UserType() {

    }
}
