package com.example.demo.repository;


import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);
    Optional<User> findByEmailAndEmailCode(String email, String emailCode);
    Optional<User> findByEmail(String email);
//    @Query(value = "select * from users join users_role ur on users.id = ur.users_id where role_id=?1 and users_id=?2",nativeQuery = true)
//   Optional<User> getUserBy(Integer roleId,UUID userId);
}
