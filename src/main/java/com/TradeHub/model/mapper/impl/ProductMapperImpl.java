package com.TradeHub.model.mapper.impl;

import com.TradeHub.model.dto.ProductRequestDTO;
import com.TradeHub.model.dto.ProductResponseDTO;
import com.TradeHub.model.entity.Product;
import com.TradeHub.model.mapper.ProductMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMapperImpl implements ProductMapper {

    private final ModelMapper modelMapper;

    public ProductMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Product toEntity(ProductRequestDTO dto) {
        return modelMapper.map(dto, Product.class);
    }

    @Override
    public ProductResponseDTO toResponseDto(Product product) {
        return modelMapper.map(product, ProductResponseDTO.class);
    }
}
