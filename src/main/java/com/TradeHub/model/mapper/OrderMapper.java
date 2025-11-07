package com.TradeHub.model.mapper;

import com.TradeHub.model.dto.OrderDTO;
import com.TradeHub.model.entity.Order;

import java.util.List;

public interface OrderMapper {
    OrderDTO toDTO(Order order);
    Order toEntity(OrderDTO orderDTO);
    List<OrderDTO> toDTOList(List<Order> orders);
    List<Order> toEntityList(List<OrderDTO> orderDTOs);
}
