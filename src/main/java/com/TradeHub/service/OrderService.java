package com.TradeHub.service;

import com.TradeHub.model.dto.DirectCheckoutRequest;
import com.TradeHub.model.entity.Order;

public interface OrderService {
    Order createOrderFromCart(Long userId);
    Order createDirectOrder(DirectCheckoutRequest request);

}
