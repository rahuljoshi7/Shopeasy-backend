package com.shopeasy.backend.dto;

import com.shopeasy.backend.entity.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/** Response shape matching React's mapOrderRow() */
@Data
public class OrderDto {
    private UUID id;
    private String status;
    private Integer routeStep;
    private List<String> route;
    private String date;           // formatted date string for the UI
    private Boolean premium;
    private ProductSnapshot product;

    @Data
    public static class ProductSnapshot {
        private String image;
        private String name;
        private BigDecimal price;
        private Integer qty;
    }

    public static OrderDto from(Order o) {
        OrderDto dto = new OrderDto();
        dto.id = o.getId();
        dto.status = o.getStatus();
        dto.routeStep = o.getRouteStep();
        dto.route = o.getRoute();
        dto.date = o.getOrderDate().toString();    // React formats this with toLocaleDateString()
        dto.premium = o.getPremium();

        ProductSnapshot snap = new ProductSnapshot();
        snap.image = o.getProductImage();
        snap.name = o.getProductName();
        snap.price = o.getPrice();
        snap.qty = o.getQty();
        dto.product = snap;

        return dto;
    }
}
