package com.codingchallenge.library_management_system_sash.repository;

import com.codingchallenge.library_management_system_sash.domain.Book;
import com.codingchallenge.library_management_system_sash.domain.BorrowRecord;
import com.codingchallenge.library_management_system_sash.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByMemberAndReturnDateIsNull(Member member);

    Optional<BorrowRecord> findByBookAndReturnDateIsNull(Book book);

    List<BorrowRecord> findByMember(Member member);
}
