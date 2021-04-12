package com.example.demo.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class RegisterDto {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    private String email;

    @NotNull
    private String password;

}
