package com.shopeasy.backend.repository;

import com.shopeasy.backend.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, UUID> {

    List<WishlistItem> findByUserIdOrderByCreatedAtAsc(UUID userId);

    Optional<WishlistItem> findByUserIdAndProductId(UUID userId, Long productId);

    boolean existsByUserIdAndProductId(UUID userId, Long productId);
}
