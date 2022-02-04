package com.telus.dsu.libraryapi.repository;

import com.telus.dsu.libraryapi.entity.BookRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRecordRepository extends JpaRepository<BookRecord, Integer> {
    BookRecord findBookRecordByTransaction(Integer transaction);
    List<BookRecord> findAllBookRecordByUserUserCode(Integer userCode);
}
