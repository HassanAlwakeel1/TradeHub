package com.TradeHub.service;

import com.TradeHub.model.dto.CartItemRequestDTO;
import com.TradeHub.model.dto.CartResponseDTO;

public interface CartService {
    CartResponseDTO getCartByUserId(Long userId);
    CartResponseDTO clearCart(Long userId);
    CartResponseDTO addItemToCart(Long userId, CartItemRequestDTO requestDTO);
    CartResponseDTO removeItemFromCart(Long userId, Long productId);
    CartResponseDTO updateItemQuantity(Long userId, Long productId, Integer quantity);
}
