package com.codingchallenge.library_management_system_sash.service;

import com.codingchallenge.library_management_system_sash.domain.Book;
import com.codingchallenge.library_management_system_sash.domain.BorrowRecord;
import com.codingchallenge.library_management_system_sash.domain.Member;
import com.codingchallenge.library_management_system_sash.exception.BusinessException;
import com.codingchallenge.library_management_system_sash.repository.BookRepository;
import com.codingchallenge.library_management_system_sash.repository.BorrowRecordRepository;
import com.codingchallenge.library_management_system_sash.repository.MemberRepository;
import com.codingchallenge.library_management_system_sash.resource.BorrowRequestResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class BorrowerServiceTest {

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private BorrowRecordRepository borrowRepo;

    @Test
    public void whenMemberHas3Books_thenBorrowThrows() {

        Member member = new Member();
        member.setName("John");
        member.setEmail("john@example.com");
        memberRepo.save(member);

        for (int i = 0; i < 3; i++) {
            Book book = new Book();
            book.setTitle("Book " + i);
            book.setAuthor("Author");
            book.setIsbn("ISBN-" + i + System.currentTimeMillis());
            book.setAvailability(false);
            bookRepo.save(book);

            BorrowRecord record = new BorrowRecord();
            record.setBook(book);
            record.setMember(member);
            record.setBorrowDate(LocalDateTime.now().minusDays(1));
            borrowRepo.save(record);
        }

        Book newBook = new Book();
        newBook.setTitle("NewTitle");
        newBook.setAuthor("Author A");
        newBook.setIsbn("ISBN-NEW-" + System.currentTimeMillis());
        newBook.setAvailability(true);
        bookRepo.save(newBook);

        BorrowRequestResource borrowRequestResource = new BorrowRequestResource();
        borrowRequestResource.setMemberId(member.getId());
        borrowRequestResource.setBookId(newBook.getId());

        Assertions.assertThrows(BusinessException.class, () -> {
            borrowService.borrowBooks(borrowRequestResource);
        });
    }
}
