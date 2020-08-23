package com.telran.phonebookapi.dto;

public class UserDetailsDto {
    public String email;
    public boolean isActive;

    public UserDetailsDto(String email, boolean isActive) {
        this.email = email;
        this.isActive = isActive;
    }
}
