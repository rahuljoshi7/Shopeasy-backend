package com.shopeasy.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Request body for POST /wishlist/toggle */
@Data
public class ToggleWishlistRequest {

    @NotNull(message = "productId is required")
    private Long productId;

    public @NotNull(message = "productId is required") Long getProductId() {
        return productId;
    }

    public void setProductId(@NotNull(message = "productId is required") Long productId) {
        this.productId = productId;
    }
}
