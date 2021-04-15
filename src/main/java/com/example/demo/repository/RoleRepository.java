package com.example.demo.repository;


import com.example.demo.entity.Role;
import com.example.demo.entity.enams.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRoleName(RoleEnum roleName);

}
