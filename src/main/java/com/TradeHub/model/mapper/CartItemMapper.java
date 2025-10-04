package com.TradeHub.model.mapper;

import com.TradeHub.model.dto.CartItemRequestDTO;
import com.TradeHub.model.dto.CartItemResponseDTO;
import com.TradeHub.model.entity.CartItem;

public interface CartItemMapper {
    CartItem toEntity(CartItemRequestDTO dto);
    CartItemResponseDTO toResponseDto(CartItem cartItem);
}
