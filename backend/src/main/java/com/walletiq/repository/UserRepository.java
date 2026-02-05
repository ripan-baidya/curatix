package com.walletiq.repository;

import com.walletiq.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Find User by Email
     */
    Optional<User> findByEmail(String email);

    /**
     * Check whether a user with Email already exist in the System
     */
    boolean existsByEmail(String email);
}
