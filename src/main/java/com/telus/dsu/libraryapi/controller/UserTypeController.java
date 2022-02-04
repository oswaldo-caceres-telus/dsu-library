package com.telus.dsu.libraryapi.controller;


import com.telus.dsu.libraryapi.entity.UserType;
import com.telus.dsu.libraryapi.entity.dto.BookDTO;
import com.telus.dsu.libraryapi.entity.dto.UserTypeDTO;
import com.telus.dsu.libraryapi.exception.ResourceNotCreatedException;
import com.telus.dsu.libraryapi.exception.ResourceNotFoundException;
import com.telus.dsu.libraryapi.service.UserTypeService;
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
@RequestMapping("/api/userType")
public class UserTypeController {

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<?> getAllUserTypes() {
        List<UserType> userTypes = userTypeService.getUserTypes();
        return new ResponseEntity<List<UserTypeDTO>>(convertListToDTO(userTypes), HttpStatus.OK);
    }

    @GetMapping("/{userTypeId}")
    public ResponseEntity<?> getUserTypeById(@PathVariable Integer userTypeId) {
        UserType userTypeFound = userTypeService.getUserTypeById(userTypeId);
        if(userTypeFound == null){
            throw new ResourceNotFoundException("userType not found with id: " + userTypeId);
        }else{
            UserTypeDTO userTypeDTO = convertToDTO(userTypeFound);
            return new ResponseEntity<UserTypeDTO>(userTypeDTO, HttpStatus.OK);
        }
    }
    @PostMapping("")
    public ResponseEntity<?> createUserType(@Valid @RequestBody UserType userType, BindingResult result){
        if(result.hasErrors()){
            throw new ResourceNotCreatedException("userType not created");
        }else{
            UserType newUserType = userTypeService.createUser(userType);
            return new ResponseEntity<>(newUserType, HttpStatus.CREATED);
        }
    }

    //TODO Create, Update and Delete

    private List<UserTypeDTO> convertListToDTO(List<UserType> userTypes){
        List<UserTypeDTO> userTypeDTOList = new ArrayList<>();
        for (UserType userType: userTypes) {
            userTypeDTOList.add(convertToDTO(userType));
        }
        return userTypeDTOList;
    }

    private UserTypeDTO convertToDTO(UserType userType) {
        return modelMapper.map(userType, UserTypeDTO.class);
    }
}
