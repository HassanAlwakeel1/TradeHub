package com.TradeHub.model.dto;

import com.TradeHub.model.entity.OrderItem;
import com.TradeHub.model.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class OrderDTO {
    private Long id;
    private Long userId;  // ✅ Only user ID, not full user object
    private List<OrderItemDTO> items;
    private double totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
