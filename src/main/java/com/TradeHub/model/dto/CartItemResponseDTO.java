package com.TradeHub.model.dto;

import lombok.Data;

@Data
public class CartItemResponseDTO {
    private Long id;
    private ProductResponseDTO productResponseDTO;
    private Integer quantity;
    private Double totalProductrPrice; // product.price * quantity
}
