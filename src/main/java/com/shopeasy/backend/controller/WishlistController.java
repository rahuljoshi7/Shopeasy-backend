package com.shopeasy.backend.controller;

import com.shopeasy.backend.dto.ToggleWishlistRequest;
import com.shopeasy.backend.dto.WishlistItemDto;
import com.shopeasy.backend.service.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    private UUID getUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    @GetMapping
    public ResponseEntity<List<WishlistItemDto>> getWishlist(
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                wishlistService.getWishlist(getUserId(jwt))
        );
    }

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleWishlist(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ToggleWishlistRequest req) {

        Map<String, Object> result =
                wishlistService.toggle(
                        getUserId(jwt),
                        req.getProductId()
                );

        return ResponseEntity.ok(result);
    }
}