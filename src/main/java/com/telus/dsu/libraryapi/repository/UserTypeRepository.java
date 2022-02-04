package com.telus.dsu.libraryapi.repository;

import com.telus.dsu.libraryapi.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Integer> {
    UserType findByUserTypeId(Integer id);
}
