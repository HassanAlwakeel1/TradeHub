package com.TradeHub.model.mapper.impl;


import com.TradeHub.model.dto.CartResponseDTO;
import com.TradeHub.model.dto.CartItemResponseDTO;
import com.TradeHub.model.entity.Cart;
import com.TradeHub.model.mapper.CartMapper;
import com.TradeHub.model.mapper.CartItemMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapperImpl implements CartMapper {

    private final ModelMapper modelMapper;
    private final CartItemMapper cartItemMapper;

    public CartMapperImpl(ModelMapper modelMapper, CartItemMapper cartItemMapper) {
        this.modelMapper = modelMapper;
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    public CartResponseDTO toResponseDTO(Cart cart) {
        CartResponseDTO dto = modelMapper.map(cart, CartResponseDTO.class);

        List<CartItemResponseDTO> items = cart.getCartItemList()
                .stream()
                .map(cartItemMapper::toResponseDto)
                .collect(Collectors.toList());

        dto.setItems(items);

        // calculate total cart price
        dto.setTotalCartPrice(
                items.stream()
                        .mapToDouble(CartItemResponseDTO::getTotalProductrPrice)
                        .sum()
        );

        return dto;
    }
}
