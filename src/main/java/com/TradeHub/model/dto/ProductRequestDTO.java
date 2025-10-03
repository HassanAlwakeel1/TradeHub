package com.TradeHub.model.dto;

import lombok.Data;
@Data
public class ProductRequestDTO {
    private String name;
    private String description;
    private Double price;
    private String category;
    private Long stockQuantity;
    private Long sellerId;
}
