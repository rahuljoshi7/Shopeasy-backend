package com.shopeasy.backend.dto;

import lombok.Data;

/** Request body for PATCH /cart/{productId}/qty */
@Data
public class UpdateQtyRequest {
    /** +1 to increment, -1 to decrement. */
    private int delta;
}
