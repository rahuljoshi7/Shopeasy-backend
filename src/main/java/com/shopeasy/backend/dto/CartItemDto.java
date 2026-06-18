package com.shopeasy.backend.dto;

import com.shopeasy.backend.entity.CartItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/** Response shape matching React's mapCartRow() */
@Data
public class CartItemDto {
    private UUID cartRowId;      // React uses this as _cartRowId
    private Long id;             // product id
    private String name;
    private String category;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private BigDecimal rating;
    private Integer discount;
    private Boolean premium;
    private String image;
    private String description;
    private Integer qty;

    public static CartItemDto from(CartItem ci) {
        CartItemDto dto = new CartItemDto();
        dto.cartRowId = ci.getId();
        dto.qty = ci.getQty();

        var p = ci.getProduct();
        dto.id = p.getId();
        dto.name = p.getName();
        dto.category = p.getCategory();
        dto.price = p.getPrice();
        dto.originalPrice = p.getOriginalPrice();
        dto.rating = p.getRating();
        dto.discount = p.getDiscount();
        dto.premium = p.getPremium();
        dto.image = p.getImage();
        dto.description = p.getDescription();
        return dto;
    }
}
