package com.shopeasy.backend.service;

import com.shopeasy.backend.dto.CartItemDto;
import com.shopeasy.backend.entity.CartItem;
import com.shopeasy.backend.repository.CartItemRepository;
import com.shopeasy.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    // ── Read ────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<CartItemDto> getCart(UUID userId) {
        return cartItemRepository.findByUserIdOrderByCreatedAtAsc(userId)
                .stream()
                .map(CartItemDto::from)
                .toList();
    }

    // ── Add / increment ─────────────────────────────────────────────────────

    /**
     * If the product is already in the cart, increments qty by 1 (matching the React addToCart).
     * Otherwise inserts a new row with qty = 1 (or the supplied qty for restore-from-undo).
     */
    @Transactional
    public CartItemDto addToCart(UUID userId, Long productId, int qty) {
        var existing = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (existing.isPresent()) {
            var ci = existing.get();
            ci.setQty(ci.getQty() + 1);
            return CartItemDto.from(cartItemRepository.save(ci));
        }

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        var ci = new CartItem();
        ci.setUserId(userId);
        ci.setProduct(product);
        ci.setQty(Math.max(1, qty));
        return CartItemDto.from(cartItemRepository.save(ci));
    }

    // ── Update quantity ──────────────────────────────────────────────────────

    /**
     * Applies a delta (+1 / -1) to the cart row identified by cartRowId.
     * Quantity is clamped to a minimum of 1 (same behaviour as React updateQty).
     */
    @Transactional
    public CartItemDto updateQty(UUID userId, UUID cartRowId, int delta) {
        var ci = cartItemRepository.findById(cartRowId)
                .filter(c -> c.getUserId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + cartRowId));

        int newQty = Math.max(1, ci.getQty() + delta);
        ci.setQty(newQty);
        return CartItemDto.from(cartItemRepository.save(ci));
    }

    // ── Remove ───────────────────────────────────────────────────────────────

    @Transactional
    public void removeFromCart(UUID userId, UUID cartRowId) {
        var ci = cartItemRepository.findById(cartRowId)
                .filter(c -> c.getUserId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + cartRowId));
        cartItemRepository.delete(ci);
    }

    // ── Clear (called after placeOrder) ──────────────────────────────────────

    @Transactional
    public void clearCart(UUID userId) {
        cartItemRepository.deleteAllByUserId(userId);
    }
}
