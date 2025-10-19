package com.codingchallenge.library_management_system_sash.controller;

import com.codingchallenge.library_management_system_sash.resource.BorrowerRecordResponseResource;
import com.codingchallenge.library_management_system_sash.resource.MemberRequestResource;
import com.codingchallenge.library_management_system_sash.resource.MemberResponseResource;
import com.codingchallenge.library_management_system_sash.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) { this.memberService = memberService; }

    @PostMapping
    public ResponseEntity<MemberResponseResource> memberRegistration(@Valid @RequestBody MemberRequestResource memberRequestResource) {
        MemberResponseResource created = memberService.createMember(memberRequestResource);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseResource> getMemberAndBorrowDetails(@PathVariable("id") Long memberId) {
        return ResponseEntity.ok(memberService.findMemberDetailsAndCurrentlyBorrowBooks(memberId));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<BorrowerRecordResponseResource>> getMemberBorrowHistory(@PathVariable("id")  Long memberId) {
        return ResponseEntity.ok(memberService.findBorrowedDetailsOfMember(memberId));
    }
}
