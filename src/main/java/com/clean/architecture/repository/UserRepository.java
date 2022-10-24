package com.clean.architecture.repository;

import com.clean.architecture.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, String> {
    Optional<UserModel> findByUsername(String username);

    Optional<UserModel> findByUsernameAndStatusNot(String username, Character status);

    @Query(value = "SELECT * FROM `USER` US WHERE (:username IS NULL OR US.USERNAME LIKE :username%)" +
            " AND (:firstName IS NULL OR US.FIRST_NAME LIKE :firstName%)", nativeQuery = true)
    Page<UserModel> getListUser(@Param(value = "username") String username, @Param(value = "firstName") String firstName, Pageable pageable);
}
