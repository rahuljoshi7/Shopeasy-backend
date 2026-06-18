package com.shopeasy.backend.service;

import com.shopeasy.backend.dto.OrderDto;
import com.shopeasy.backend.entity.Order;
import com.shopeasy.backend.repository.CartItemRepository;
import com.shopeasy.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    /**
     * Two delivery routes, alternated across cart items – same logic as the React app.
     */
    private static final List<List<String>> ROUTES = List.of(
            List.of("Warehouse", "Hub", "Your City", "Delivery"),
            List.of("Warehouse", "Airport", "Your City", "Hub", "Delivery")
    );

    private static final List<String> STATUSES =
            List.of("Processing", "Shipped", "In Transit", "Out for Delivery", "Delivered");

    // ── Read ────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<OrderDto> getOrders(UUID userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(OrderDto::from)
                .toList();
    }

    // ── Place order ─────────────────────────────────────────────────────────

    /**
     * Converts every cart item into its own Order row (each gets its own
     * shipment + delivery route), then clears the cart.
     * Mirrors the React placeOrder() exactly.
     */
    @Transactional
    public List<OrderDto> placeOrder(UUID userId) {
        var cartItems = cartItemRepository.findByUserIdOrderByCreatedAtAsc(userId);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot place an order with an empty cart");
        }

        List<Order> newOrders = new java.util.ArrayList<>();

        for (int i = 0; i < cartItems.size(); i++) {
            var ci = cartItems.get(i);
            var product = ci.getProduct();

            Order order = new Order();
            order.setUserId(userId);
            order.setProduct(product);
            order.setProductName(product.getName());
            order.setProductImage(product.getImage());
            order.setPrice(product.getPrice());
            order.setQty(ci.getQty());
            order.setPremium(product.getPremium());
            order.setStatus("Processing");
            order.setRoute(ROUTES.get(i % ROUTES.size()));
            order.setRouteStep(0);

            newOrders.add(order);
        }

        List<Order> saved = orderRepository.saveAll(newOrders);

        // Clear the cart after successful order creation
        cartItemRepository.deleteAllByUserId(userId);

        return saved.stream().map(OrderDto::from).toList();
    }

    // ── Advance route ────────────────────────────────────────────────────────

    /**
     * Increments the route_step by 1 (clamped to route length - 1) and
     * advances the status through the STATUSES list.
     * Mirrors the React advanceRoute() exactly.
     */
    @Transactional
    public OrderDto advanceRoute(UUID userId, UUID orderId) {
        var order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        int next = order.getRouteStep() + 1;
        int newRouteStep = Math.min(next, order.getRoute().size() - 1);
        String newStatus = STATUSES.get(Math.min(next, STATUSES.size() - 1));

        order.setRouteStep(newRouteStep);
        order.setStatus(newStatus);

        return OrderDto.from(orderRepository.save(order));
    }
}
