package com.TradeHub.model.mapper;

import com.TradeHub.model.dto.CartResponseDTO;
import com.TradeHub.model.entity.Cart;

public interface CartMapper {
    CartResponseDTO toResponseDTO(Cart cart);
}
