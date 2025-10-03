package com.TradeHub.model.dto;

import lombok.Data;

@Data
public class ProductUpdateDTO {
    private String name;
    private String description;
    private Double price;
    private String category;
    private Long stockQuantity;
}
