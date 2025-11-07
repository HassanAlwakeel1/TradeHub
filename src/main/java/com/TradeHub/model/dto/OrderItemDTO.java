package com.TradeHub.model.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private Long productId;  // ✅ Only product ID
    private String productName; // Optional for convenience
    private double price;
    private int quantity;
}