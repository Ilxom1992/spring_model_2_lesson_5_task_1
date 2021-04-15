package com.example.demo.entity;

import com.example.demo.entity.enams.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;


import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;
    @Override
    public String getAuthority() {
        return roleName.name();
    }
}
