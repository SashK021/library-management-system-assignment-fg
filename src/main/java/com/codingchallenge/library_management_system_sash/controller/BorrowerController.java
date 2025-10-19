package com.codingchallenge.library_management_system_sash.controller;

import com.codingchallenge.library_management_system_sash.resource.BorrowRequestResource;
import com.codingchallenge.library_management_system_sash.resource.BorrowerRecordResponseResource;
import com.codingchallenge.library_management_system_sash.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
public class BorrowerController {

    private final BorrowService borrowService;

    public BorrowerController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping
    public ResponseEntity<BorrowerRecordResponseResource> borrow(@Valid @RequestBody BorrowRequestResource borrowRequestResource) {
        BorrowerRecordResponseResource borrowerRecordResponseResource = borrowService.borrowBooks(borrowRequestResource);
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowerRecordResponseResource);
    }

    @PostMapping("/{id}")
    public ResponseEntity<BorrowerRecordResponseResource> returnBook(@PathVariable("id")  Long borrowerId) {
        BorrowerRecordResponseResource resp = borrowService.returnBooks(borrowerId);
        return ResponseEntity.ok(resp);
    }
}

