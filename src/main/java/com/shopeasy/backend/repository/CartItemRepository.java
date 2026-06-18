package com.shopeasy.backend.repository;

import com.shopeasy.backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    List<CartItem> findByUserIdOrderByCreatedAtAsc(UUID userId);

    Optional<CartItem> findByUserIdAndProductId(UUID userId, Long productId);

    boolean existsByUserIdAndProductId(UUID userId, Long productId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.userId = :userId")
    void deleteAllByUserId(UUID userId);
}
