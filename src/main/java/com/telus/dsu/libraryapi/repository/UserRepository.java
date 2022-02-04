package com.telus.dsu.libraryapi.repository;

import com.telus.dsu.libraryapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserCode(Integer userCode);
    User findByUserId(Integer id);
}
