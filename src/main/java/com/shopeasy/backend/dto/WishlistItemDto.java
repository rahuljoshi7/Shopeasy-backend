package com.shopeasy.backend.dto;

import com.shopeasy.backend.entity.WishlistItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/** Response shape matching React's mapWishlistRow() */
@Data
public class WishlistItemDto {
    private UUID wishRowId;      // React uses this as _wishRowId
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

    public static WishlistItemDto from(WishlistItem wi) {
        WishlistItemDto dto = new WishlistItemDto();
        dto.wishRowId = wi.getId();

        var p = wi.getProduct();
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
