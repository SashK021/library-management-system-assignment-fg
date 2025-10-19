package com.codingchallenge.library_management_system_sash.service.serviceImpl;

import com.codingchallenge.library_management_system_sash.domain.BorrowRecord;
import com.codingchallenge.library_management_system_sash.domain.Member;
import com.codingchallenge.library_management_system_sash.exception.BusinessException;
import com.codingchallenge.library_management_system_sash.exception.NotFoundException;
import com.codingchallenge.library_management_system_sash.repository.BorrowRecordRepository;
import com.codingchallenge.library_management_system_sash.repository.MemberRepository;
import com.codingchallenge.library_management_system_sash.resource.BookResponseResource;
import com.codingchallenge.library_management_system_sash.resource.BorrowerRecordResponseResource;
import com.codingchallenge.library_management_system_sash.resource.MemberRequestResource;
import com.codingchallenge.library_management_system_sash.resource.MemberResponseResource;
import com.codingchallenge.library_management_system_sash.service.MemberService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final BorrowRecordRepository borrowRecordRepository;

    public MemberServiceImpl(MemberRepository memberRepository, BorrowRecordRepository borrowRecordRepository) {
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Override
    public MemberResponseResource createMember(MemberRequestResource memberRequestResource) {
        if (memberRepository.findByEmail(memberRequestResource.getEmail()).isPresent()) {
            throw new BusinessException("Email already exists");
        }
        Member member = new Member();
        member.setName(memberRequestResource.getName());
        member.setEmail(memberRequestResource.getEmail());
        member.setMemberShipDate(LocalDate.now());
        memberRepository.save(member);
        return createMemberResponse(member);
    }

    @Override
    public MemberResponseResource findMemberDetailsAndCurrentlyBorrowBooks(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("Member details not found"));
        List<BorrowRecord> current = borrowRecordRepository.findByMemberAndReturnDateIsNull(member);
        MemberResponseResource memberResponseResource = memberResponse(member);
        memberResponseResource.setCurrentlyBorrowed(current.stream().map(br -> {
            BookResponseResource bookResponseResource = new BookResponseResource();
            bookResponseResource.setId(br.getBook().getId());
            bookResponseResource.setTitle(br.getBook().getTitle());
            bookResponseResource.setAuthor(br.getBook().getAuthor());
            bookResponseResource.setIsbn(br.getBook().getIsbn());
            bookResponseResource.setAvailability(br.getBook().getAvailability());
            return bookResponseResource;
        }).toList());
        return memberResponseResource;
    }

    @Override
    public List<BorrowerRecordResponseResource> findBorrowedDetailsOfMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);

        if (member.isPresent()) {
            List<BorrowRecord> records = borrowRecordRepository.findByMember(member.get());
            return records.stream()
                    .map(this::borrowRecordToResponse)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private MemberResponseResource memberResponse(Member member) {
        MemberResponseResource memberResponseResource = new MemberResponseResource();
        memberResponseResource.setId(member.getId());
        memberResponseResource.setName(member.getName());
        memberResponseResource.setEmail(member.getEmail());
        memberResponseResource.setMembershipDates(member.getMemberShipDate());
        return memberResponseResource;
    }

    private MemberResponseResource createMemberResponse(Member member) {
        MemberResponseResource memberResponseResource = new MemberResponseResource();
        memberResponseResource.setName(member.getName());
        memberResponseResource.setMembershipDates(member.getMemberShipDate());
        return memberResponseResource;
    }

    private BorrowerRecordResponseResource borrowRecordToResponse(BorrowRecord record) {
        BorrowerRecordResponseResource response = new BorrowerRecordResponseResource();
        response.setMemberId(record.getMember().getId());
        response.setMemberName(record.getMember().getName());
        response.setBookName(record.getBook().getTitle());
        response.setBorrowDate(record.getBorrowDate());
        response.setReturnDate(record.getReturnDate());
        return response;
    }
}
