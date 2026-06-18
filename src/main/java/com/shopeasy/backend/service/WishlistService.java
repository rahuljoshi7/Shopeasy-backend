package com.shopeasy.backend.service;

import com.shopeasy.backend.dto.WishlistItemDto;
import com.shopeasy.backend.entity.WishlistItem;
import com.shopeasy.backend.repository.ProductRepository;
import com.shopeasy.backend.repository.WishlistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<WishlistItemDto> getWishlist(UUID userId) {
        return wishlistItemRepository.findByUserIdOrderByCreatedAtAsc(userId)
                .stream()
                .map(WishlistItemDto::from)
                .toList();
    }

    /**
     * If the product is already wishlisted → removes it and returns {action: "removed"}.
     * If not → adds it and returns {action: "added", item: WishlistItemDto}.
     * Mirrors the React toggleWishlist() exactly.
     */
    @Transactional
    public Map<String, Object> toggle(UUID userId, Long productId) {
        var existing = wishlistItemRepository.findByUserIdAndProductId(userId, productId);

        if (existing.isPresent()) {
            wishlistItemRepository.delete(existing.get());
            return Map.of("action", "removed", "productId", productId);
        }

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        var wi = new WishlistItem();
        wi.setUserId(userId);
        wi.setProduct(product);
        var saved = wishlistItemRepository.save(wi);
        return Map.of("action", "added", "item", WishlistItemDto.from(saved));
    }
}
