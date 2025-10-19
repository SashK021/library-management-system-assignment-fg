package com.codingchallenge.library_management_system_sash.controller;

import com.codingchallenge.library_management_system_sash.resource.BookRequestResource;
import com.codingchallenge.library_management_system_sash.resource.BookResponseResource;
import com.codingchallenge.library_management_system_sash.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookResponseResource> addBook(@Valid @RequestBody BookRequestResource bookRequestResource) {
        BookResponseResource created = bookService.addBooks(bookRequestResource);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<BookResponseResource>> searchBooks(
            @RequestParam(required = false) Boolean available,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookService.searchBooks(available, pageable, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseResource> findBookById(@PathVariable("id") Long bookId) {
        return ResponseEntity.ok(bookService.findBookById(bookId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeBook(@PathVariable("id")  Long bookId) {
        bookService.removeBook(bookId);
        return ResponseEntity.noContent().build();
    }

}
