package com.shopeasy.backend.dto;

import com.shopeasy.backend.entity.Product;
import lombok.Data;

import java.math.BigDecimal;

/** Response shape matching the React app's mapProduct() */
@Data
public class ProductDto {
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private BigDecimal rating;
    private Integer discount;
    private Boolean premium;
    private String image;
    private String description;

    public static ProductDto from(Product p) {
        ProductDto dto = new ProductDto();
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
