package com.telus.dsu.libraryapi.service;

import com.telus.dsu.libraryapi.entity.UserType;
import com.telus.dsu.libraryapi.exception.ResourceNotCreatedException;
import com.telus.dsu.libraryapi.repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeService {

    @Autowired
    private UserTypeRepository userTypeRepository;

    public List<UserType> getUserTypes() {
        return userTypeRepository.findAll();
    }

    public UserType getUserTypeById(Integer userTypeId) {
        return userTypeRepository.findByUserTypeId(userTypeId);
    }

    public UserType createUser(UserType userType){
        try{
            return userTypeRepository.save(userType);
        }catch (Exception e){
            throw new ResourceNotCreatedException("User type "+userType.getUserType()+" already exist");
        }

    }

    //TODO Create, Delete and Update
}
