package com.example.demo.payload;

import com.example.demo.entity.Role;
import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    private List<Integer> roleListId;
    private String roleName;
}
