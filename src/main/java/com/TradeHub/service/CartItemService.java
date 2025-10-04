package com.TradeHub.service;

import com.TradeHub.model.dto.CartItemRequestDTO;
import com.TradeHub.model.dto.CartResponseDTO;

public interface CartItemService {
    CartResponseDTO addItemToCart(Long userId, CartItemRequestDTO requestDTO);
    CartResponseDTO updateItemQuantity(Long userId, Long productId, Integer quantity);
    CartResponseDTO removeItemFromCart(Long userId, Long productId);
}
