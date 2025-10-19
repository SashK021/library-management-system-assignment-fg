package com.codingchallenge.library_management_system_sash.service.serviceImpl;

import com.codingchallenge.library_management_system_sash.domain.Book;
import com.codingchallenge.library_management_system_sash.domain.BorrowRecord;
import com.codingchallenge.library_management_system_sash.domain.Member;
import com.codingchallenge.library_management_system_sash.exception.BusinessException;
import com.codingchallenge.library_management_system_sash.exception.NotFoundException;
import com.codingchallenge.library_management_system_sash.repository.BookRepository;
import com.codingchallenge.library_management_system_sash.repository.BorrowRecordRepository;
import com.codingchallenge.library_management_system_sash.repository.MemberRepository;
import com.codingchallenge.library_management_system_sash.resource.BorrowRequestResource;
import com.codingchallenge.library_management_system_sash.resource.BorrowerRecordResponseResource;
import com.codingchallenge.library_management_system_sash.service.BorrowService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    private static final int MAX_BORROW = 3;

    public BorrowServiceImpl(BorrowRecordRepository borrowRecordRepository, MemberRepository memberRepository, BookRepository bookRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public BorrowerRecordResponseResource borrowBooks(BorrowRequestResource borrowRequestResource) {
        Member member = memberRepository.findById(borrowRequestResource.getMemberId()).orElseThrow(() -> new NotFoundException("Member details not available"));
        Book book = bookRepository.findById(borrowRequestResource.getBookId()).orElseThrow(() -> new NotFoundException("Book not available"));

        List<BorrowRecord> current = borrowRecordRepository.findByMemberAndReturnDateIsNull(member);
        if (current.size() >= MAX_BORROW) throw new BusinessException("Member cannot borrow any more books.");

        if (!Boolean.TRUE.equals(book.getAvailability())) throw new BusinessException("Book is not available");

        book.setAvailability(false);
        bookRepository.save(book);

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBook(book);
        borrowRecord.setMember(member);
        borrowRecord.setBorrowDate(LocalDateTime.now());
        borrowRecord = borrowRecordRepository.save(borrowRecord);

        return createBorrowRecord(borrowRecord, book.getTitle(), member.getName());
    }

    @Override
    public BorrowerRecordResponseResource returnBooks(Long borrowerId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowerId).orElseThrow(() -> new NotFoundException("No details of borrowing found."));
        if (borrowRecord.getReturnDate() != null) throw new BusinessException("The book has already been returned.");

        borrowRecord.setReturnDate(LocalDateTime.now());
        borrowRecord.getBook().setAvailability(true);
        bookRepository.save(borrowRecord.getBook());
        borrowRecordRepository.save(borrowRecord);
        return returnBorrowedBook(borrowRecord);
    }

    private BorrowerRecordResponseResource createBorrowRecord(BorrowRecord borrowRecord, String bookName, String memberName){
        BorrowerRecordResponseResource borrowerRecordResponseResource = new BorrowerRecordResponseResource();
        borrowerRecordResponseResource.setBorrowDate(borrowRecord.getBorrowDate());
        borrowerRecordResponseResource.setBookName(bookName);
        borrowerRecordResponseResource.setMemberName(memberName);
        return borrowerRecordResponseResource;
    }

    private BorrowerRecordResponseResource returnBorrowedBook(BorrowRecord borrowRecord){
        BorrowerRecordResponseResource borrowerRecordResponseResource = new BorrowerRecordResponseResource();
        borrowerRecordResponseResource.setReturnDate(borrowRecord.getReturnDate());
        borrowerRecordResponseResource.setBookName(borrowRecord.getBook().getTitle());
        borrowerRecordResponseResource.setMemberName(borrowRecord.getMember().getName());
        return borrowerRecordResponseResource;
    }
}
