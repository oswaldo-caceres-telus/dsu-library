package com.telus.dsu.libraryapi.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
    private Integer userCode;
    private String firstName;
    private String lastName;
    private Integer borrowedBooks;
    private String email;
    private String phone;
    private Boolean isActive;
    private Integer userTypeId;

    public UserDTO() {
    }

    //TODO UserType
}
