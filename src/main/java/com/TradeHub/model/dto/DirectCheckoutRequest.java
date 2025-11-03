package com.TradeHub.model.dto;

import lombok.Data;

@Data
public class DirectCheckoutRequest {
    private Long userId;
    private Long productId;
    private String productName;
    private String description;
    private Double price;
    private Integer quantity;
}
