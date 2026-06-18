package com.shopeasy.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Request body for POST /cart */
@Data
public class AddToCartRequest {

    @NotNull(message = "productId is required")
    private Long productId;

    @Min(value = 1, message = "qty must be at least 1")
    private int qty = 1;
}
