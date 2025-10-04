package com.TradeHub.model.mapper.impl;

import com.TradeHub.model.dto.CartItemRequestDTO;
import com.TradeHub.model.dto.CartItemResponseDTO;
import com.TradeHub.model.entity.CartItem;
import com.TradeHub.model.mapper.CartItemMapper;
import com.TradeHub.model.mapper.ProductMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapperImpl implements CartItemMapper {

    private final ModelMapper modelMapper;
    private final ProductMapper productMapper;

    public CartItemMapperImpl(ModelMapper modelMapper, ProductMapper productMapper) {
        this.modelMapper = modelMapper;
        this.productMapper = productMapper;
    }

    @Override
    public CartItem toEntity(CartItemRequestDTO dto) {
        return modelMapper.map(dto, CartItem.class);
        // ⚠ product will be set in service layer
    }

    @Override
    public CartItemResponseDTO toResponseDto(CartItem cartItem) {
        CartItemResponseDTO dto = modelMapper.map(cartItem, CartItemResponseDTO.class);
        dto.setProductResponseDTO(productMapper.toResponseDto(cartItem.getProduct()));
        dto.setTotalProductrPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        return dto;
    }
}
