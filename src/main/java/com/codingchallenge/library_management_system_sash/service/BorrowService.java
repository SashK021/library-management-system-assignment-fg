package com.codingchallenge.library_management_system_sash.service;

import com.codingchallenge.library_management_system_sash.resource.BookRequestResource;
import com.codingchallenge.library_management_system_sash.resource.BorrowRequestResource;
import com.codingchallenge.library_management_system_sash.resource.BorrowerRecordResponseResource;
import org.springframework.stereotype.Service;

@Service
public interface BorrowService {

    BorrowerRecordResponseResource borrowBooks(BorrowRequestResource borrowRequestResource);

    BorrowerRecordResponseResource returnBooks(Long borrowerId);

}
