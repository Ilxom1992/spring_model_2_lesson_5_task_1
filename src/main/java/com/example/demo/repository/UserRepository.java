package com.example.demo.repository;


import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);
    Optional<User> findByEmailAndEmailCode(String email, String emailCode);
    Optional<User> findByEmail(String email);
   @Query(value = "select  * from users join task t on users.id = t.responsible_id where t.task_code = ?1",nativeQuery = true)
   Optional<User> getUserByTaskCode(String taskCode);
   List<User> findAllByRolesIn(Collection<Set<Role>> roles);
   @Query(value = "select * from users join users_roles ur on users.id = ur.users_id where ur.roles_id= ?1",nativeQuery = true)
    List<User> getUserByRolesId(Integer rolesId);
   @Query(value = "select * from users join users_roles ur on users.id = ur.users_id where ur.roles_id between 2 and 4 ",nativeQuery = true)
    List<User> getUserByRolesIdDirector();


}
