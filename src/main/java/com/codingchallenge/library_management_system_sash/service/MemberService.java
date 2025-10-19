package com.codingchallenge.library_management_system_sash.service;

import com.codingchallenge.library_management_system_sash.resource.BorrowerRecordResponseResource;
import com.codingchallenge.library_management_system_sash.resource.MemberRequestResource;
import com.codingchallenge.library_management_system_sash.resource.MemberResponseResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MemberService {

    MemberResponseResource createMember(MemberRequestResource memberRequestResource);

    MemberResponseResource findMemberDetailsAndCurrentlyBorrowBooks(Long memberId);

    List<BorrowerRecordResponseResource> findBorrowedDetailsOfMember(Long memberId);
}
