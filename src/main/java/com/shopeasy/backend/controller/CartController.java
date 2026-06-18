package com.shopeasy.backend.controller;

import com.shopeasy.backend.dto.AddToCartRequest;
import com.shopeasy.backend.dto.CartItemDto;
import com.shopeasy.backend.dto.UpdateQtyRequest;
import com.shopeasy.backend.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private UUID getUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCart(
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                cartService.getCart(getUserId(jwt))
        );
    }

    @PostMapping
    public ResponseEntity<CartItemDto> addToCart(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AddToCartRequest req) {

        CartItemDto item = cartService.addToCart(
                getUserId(jwt),
                req.getProductId(),
                req.getQty()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(item);
    }

    @PatchMapping("/{cartRowId}/qty")
    public ResponseEntity<CartItemDto> updateQty(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID cartRowId,
            @RequestBody UpdateQtyRequest req) {

        return ResponseEntity.ok(
                cartService.updateQty(
                        getUserId(jwt),
                        cartRowId,
                        req.getDelta()
                )
        );
    }

    @DeleteMapping("/{cartRowId}")
    public ResponseEntity<Void> removeFromCart(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID cartRowId) {

        cartService.removeFromCart(
                getUserId(jwt),
                cartRowId
        );

        return ResponseEntity.noContent().build();
    }
}