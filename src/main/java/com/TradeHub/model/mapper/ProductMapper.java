package com.TradeHub.model.mapper;

import com.TradeHub.model.dto.ProductRequestDTO;
import com.TradeHub.model.dto.ProductResponseDTO;
import com.TradeHub.model.entity.Product;

public interface ProductMapper {
    Product toEntity(ProductRequestDTO dto);
    ProductResponseDTO toResponseDto(Product product);
}
