package com.TradeHub.model.dto;

import lombok.Data;

@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Long stockQuantity;
    private String imageUrl;
    private Long sellerId;

}
