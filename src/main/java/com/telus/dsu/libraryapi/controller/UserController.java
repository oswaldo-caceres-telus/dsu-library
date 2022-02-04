package com.telus.dsu.libraryapi.controller;

import com.telus.dsu.libraryapi.entity.User;
import com.telus.dsu.libraryapi.entity.dto.UserDTO;
import com.telus.dsu.libraryapi.exception.ResourceNotCreatedException;
import com.telus.dsu.libraryapi.exception.ResourceNotFoundException;
import com.telus.dsu.libraryapi.service.UserService;
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
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<?> getAllBooks(){
        List<User> users = userService.getUsers();
        return new ResponseEntity<List<UserDTO>>(convertListToDTO(users), HttpStatus.OK);
    }

    @GetMapping("/{userCode}")
    public ResponseEntity<?> getBookByCode(@PathVariable Integer userCode){
        User userFound = userService.getUserByCode(userCode);
        if(userFound == null){
            throw new ResourceNotFoundException("user not found with Used Code: " + userCode);
        }else{
            UserDTO userDTO = convertToDTO(userFound);
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createNewUser(@Valid @RequestBody User user, BindingResult result){
        if(result.hasErrors()){
            throw new ResourceNotCreatedException("User was not created");
        }else{
            User newUser = userService.createUser(user);
            return new ResponseEntity<>(convertToDTO(newUser), HttpStatus.CREATED);
        }
    }

    @PutMapping("/{userCode}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user, BindingResult result, @PathVariable Integer userCode){
        User userToUpdate = userService.getUserByCode(userCode);

        if(result.hasErrors() || userToUpdate == null){
            throw new ResourceNotFoundException("user not found with User Code: " + userCode);
        }else{
            User updatedUser = userService.updateUser(userToUpdate, user);
            return new ResponseEntity<UserDTO>(convertToDTO(updatedUser), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{userCode}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userCode){
        userService.deleteUser(userCode);
        return new ResponseEntity<>("User with UserCode " + userCode + "deleted", HttpStatus.OK);
    }

    @PutMapping("/deactivate/{userCode}")
    public ResponseEntity<?> deactivateUser(@PathVariable Integer userCode) {
        userService.deactivateUser(userCode);
        return new ResponseEntity<>("User with UserCode " + userCode + "deactivated", HttpStatus.OK);
    }

    private List<UserDTO> convertListToDTO(List<User> users){
        List<UserDTO> userDTOList = new ArrayList<>();
        for(User user : users){
            userDTOList.add(convertToDTO(user));
        }
        return userDTOList;
    }

    private UserDTO convertToDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}
