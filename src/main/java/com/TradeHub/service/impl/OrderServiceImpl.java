package com.TradeHub.service.impl;

import com.TradeHub.model.dto.CartItemDTO;
import com.TradeHub.model.dto.CartResponseDTO;
import com.TradeHub.model.dto.DirectCheckoutRequest;
import com.TradeHub.model.entity.*;
import com.TradeHub.model.entity.enums.OrderStatus;
import com.TradeHub.repository.*;
import com.TradeHub.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Override
    public Order createOrderFromCart(Long userId) {
        // 1️⃣ Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // 2️⃣ Get user's cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user with id: " + userId));

        List<CartItem> cartItems = cart.getCartItemList();
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot create order");
        }

        // 3️⃣ Create Order and map cart items → order items
        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (CartItem cartItem : cartItems) {
            double itemTotal = cartItem.getProduct().getPrice() * cartItem.getQuantity();
            totalPrice += itemTotal;

            OrderItem orderItem = OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getProduct().getPrice()) // ✅ FIX: store unit price
                    .build();

            orderItems.add(orderItem);
        }

        Order order = Order.builder()
                .user(user)
                .items(orderItems)
                .totalPrice(totalPrice)
                .status(OrderStatus.PENDING)
                .build();

        // 4️⃣ Set back-reference for order items
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }

        // 5️⃣ Save order to DB
        Order savedOrder = orderRepository.save(order);

        // 6️⃣ Optionally clear cart after checkout
        cart.getCartItemList().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    @Override
    public Order createDirectOrder(DirectCheckoutRequest request) {
        // 1️⃣ Find user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        // 2️⃣ Find product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        // 3️⃣ Create a new Order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        double totalPrice = product.getPrice() * request.getQuantity();
        order.setTotalPrice(totalPrice);

        // 4️⃣ Create an OrderItem and link it
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(request.getQuantity());
        orderItem.setPrice(product.getPrice());
        orderItem.setOrder(order);

        // add the order item to the order’s items list
        order.getItems().add(orderItem);

        // 5️⃣ Save and return
        return orderRepository.save(order);
    }
}
