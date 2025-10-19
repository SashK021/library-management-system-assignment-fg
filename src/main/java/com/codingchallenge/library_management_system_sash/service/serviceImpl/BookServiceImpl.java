package com.codingchallenge.library_management_system_sash.service.serviceImpl;

import com.codingchallenge.library_management_system_sash.domain.Book;
import com.codingchallenge.library_management_system_sash.domain.BorrowRecord;
import com.codingchallenge.library_management_system_sash.exception.BusinessException;
import com.codingchallenge.library_management_system_sash.exception.NotFoundException;
import com.codingchallenge.library_management_system_sash.repository.BookRepository;
import com.codingchallenge.library_management_system_sash.repository.BorrowRecordRepository;
import com.codingchallenge.library_management_system_sash.resource.BookRequestResource;
import com.codingchallenge.library_management_system_sash.resource.BookResponseResource;
import com.codingchallenge.library_management_system_sash.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BorrowRecordRepository borrowRecordRepository;

    public BookServiceImpl(BookRepository bookRepository, BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Override
    public BookResponseResource addBooks(BookRequestResource bookRequestResource) {
        if(bookRepository.findByIsbn(bookRequestResource.getIsbn()).isPresent()){
            throw new BusinessException("ISBN already exists");
        }

        Book book = new Book();
        book.setTitle(bookRequestResource.getTitle());
        book.setAuthor(bookRequestResource.getAuthor());
        book.setIsbn(bookRequestResource.getIsbn());
        book.setAvailability(true);
        bookRepository.save(book);
        return createBookResponseResource(book);
    }

    @Override
    public Page<BookResponseResource> searchBooks(Boolean available, Pageable pageable, String search) {
        Page<Book> page;
        if (search != null && !search.isBlank()) {
            page = bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(search, search, pageable);
        } else if (available != null) {
            page = bookRepository.findByAvailability(available, pageable);
        } else {
            page = bookRepository.findAll(pageable);
        }
        return page.map(this::bookResponseResource);
    }

    @Override
    public BookResponseResource findBookById(Long bookId) {
        return bookResponseResource(Objects.requireNonNull(bookRepository.findById(bookId).orElse(null)));
    }

    @Override
    public void removeBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("The book is not available"));
        Optional<BorrowRecord> active = borrowRecordRepository.findByBookAndReturnDateIsNull(book);
        if (active.isPresent()) throw new BusinessException("The book is currently borrowed by someone and can be removed once it is returned.");
        bookRepository.delete(book);
    }

    private BookResponseResource createBookResponseResource(Book book){
        BookResponseResource bookResponseResource = new BookResponseResource();
        bookResponseResource.setTitle(book.getTitle());
        bookResponseResource.setIsbn(book.getIsbn());
        return bookResponseResource;
    }

    private BookResponseResource bookResponseResource(Book book){
        BookResponseResource bookResponseResource = new BookResponseResource();
        bookResponseResource.setId(book.getId());
        bookResponseResource.setTitle(book.getTitle());
        bookResponseResource.setTitle(book.getAuthor());
        bookResponseResource.setIsbn(book.getIsbn());
        bookResponseResource.setAvailability(book.getAvailability());
        return bookResponseResource;
    }
}
