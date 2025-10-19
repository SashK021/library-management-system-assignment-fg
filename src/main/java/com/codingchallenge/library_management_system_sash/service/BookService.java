package com.codingchallenge.library_management_system_sash.service;

import com.codingchallenge.library_management_system_sash.resource.BookRequestResource;
import com.codingchallenge.library_management_system_sash.resource.BookResponseResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface BookService {

    BookResponseResource addBooks(BookRequestResource bookRequestResource);

    Page<BookResponseResource> searchBooks(Boolean available, Pageable pageable, String search);

    BookResponseResource findBookById(Long bookId);

    void removeBook(Long bookId);

}
