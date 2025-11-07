package com.TradeHub.model.mapper.impl;

import com.TradeHub.model.dto.OrderDTO;
import com.TradeHub.model.dto.OrderItemDTO;
import com.TradeHub.model.entity.Order;
import com.TradeHub.model.entity.OrderItem;
import com.TradeHub.model.mapper.OrderMapper;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapperImpl implements OrderMapper {
    private final ModelMapper modelMapper;

    public OrderMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // ✅ Add this to control what ModelMapper copies
    @PostConstruct
    public void setupMappings() {
        // Map Order → OrderDTO
        modelMapper.typeMap(Order.class, OrderDTO.class)
                .addMapping(src -> src.getUser().getId(), OrderDTO::setUserId);

        // Map OrderItem → OrderItemDTO
        modelMapper.typeMap(OrderItem.class, OrderItemDTO.class)
                .addMapping(src -> src.getProduct().getId(), OrderItemDTO::setProductId)
                .addMapping(src -> src.getProduct().getName(), OrderItemDTO::setProductName);
    }

    @Override
    public OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public Order toEntity(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }
        return modelMapper.map(orderDTO, Order.class);
    }

    @Override
    public List<OrderDTO> toDTOList(List<Order> orders) {
        if (orders == null) {
            return null;
        }
        return orders.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> toEntityList(List<OrderDTO> orderDTOs) {
        if (orderDTOs == null) {
            return null;
        }
        return orderDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}