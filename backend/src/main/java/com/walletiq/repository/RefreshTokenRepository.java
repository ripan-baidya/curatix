package com.walletiq.repository;

import com.walletiq.entity.RefreshToken;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<@NonNull RefreshToken, @NonNull String> {
}
