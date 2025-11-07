package com.TradeHub.controller;

import com.TradeHub.model.dto.OrderDTO;
import com.TradeHub.model.entity.enums.OrderStatus;
import com.TradeHub.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    // 🧾 Get order by ID
    //@PreAuthorize("hasAnyRole('ADMIN','SELLER','USER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long orderId) {
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDTO);
    }

    // 🚚 Mark order as SHIPPED
    //@PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    @PutMapping("/{orderId}/ship")
    public ResponseEntity<OrderDTO> markAsShipped(@PathVariable Long orderId) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED);
        return ResponseEntity.ok(updatedOrder);
    }

    // 📦 Mark order as DELIVERED
    //@PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<OrderDTO> markAsDelivered(@PathVariable Long orderId) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, OrderStatus.DELIVERED);
        return ResponseEntity.ok(updatedOrder);
    }
    // ❌ Cancel order
    //PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
        return ResponseEntity.ok(updatedOrder);
    }
}