package com.codingchallenge.library_management_system_sash.resource;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponseResource {

    private Long id;
    private String name;
    private String email;
    private LocalDate membershipDates;
    private List<BookResponseResource> currentlyBorrowed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getMembershipDates() {
        return membershipDates;
    }

    public void setMembershipDates(LocalDate membershipDates) {
        this.membershipDates = membershipDates;
    }

    public List<BookResponseResource> getCurrentlyBorrowed() {
        return currentlyBorrowed;
    }

    public void setCurrentlyBorrowed(List<BookResponseResource> currentlyBorrowed) {
        this.currentlyBorrowed = currentlyBorrowed;
    }
}
