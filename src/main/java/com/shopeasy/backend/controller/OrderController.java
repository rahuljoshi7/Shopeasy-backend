package com.shopeasy.backend.controller;

import com.shopeasy.backend.dto.OrderDto;
import com.shopeasy.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private UUID getUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders(
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                orderService.getOrders(getUserId(jwt))
        );
    }

    @PostMapping
    public ResponseEntity<List<OrderDto>> placeOrder(
            @AuthenticationPrincipal Jwt jwt) {

        List<OrderDto> newOrders =
                orderService.placeOrder(getUserId(jwt));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newOrders);
    }

    @PostMapping("/{orderId}/advance")
    public ResponseEntity<OrderDto> advanceRoute(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                orderService.advanceRoute(
                        getUserId(jwt),
                        orderId
                )
        );
    }
}