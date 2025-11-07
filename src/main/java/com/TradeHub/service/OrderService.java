package com.TradeHub.service;

import com.TradeHub.model.dto.DirectCheckoutRequest;
import com.TradeHub.model.dto.OrderDTO;
import com.TradeHub.model.entity.Order;
import com.TradeHub.model.entity.enums.OrderStatus;

public interface OrderService {
    Order createOrderFromCart(Long userId);
    Order createDirectOrder(DirectCheckoutRequest request);
    OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus);
    OrderDTO getOrderById(Long orderId);
}
