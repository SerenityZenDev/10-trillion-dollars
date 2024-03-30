package org.example.tentrilliondollars.user.dto;

import org.example.tentrilliondollars.user.entity.UserRoleEnum;

public class UserResponseDto {

    private String username;
    private String email;
    private UserRoleEnum role;

    public UserResponseDto(String username, String email, UserRoleEnum role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
