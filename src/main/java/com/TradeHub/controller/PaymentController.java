package com.TradeHub.controller;

import com.TradeHub.model.dto.CartResponseDTO;
import com.TradeHub.model.dto.DirectCheckoutRequest;
import com.TradeHub.model.entity.Order;
import com.TradeHub.service.CartService;
import com.TradeHub.service.OrderService;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment")
@CrossOrigin
public class PaymentController {

    private final CartService cartService;
    private final OrderService orderService;
    private static final Logger logger = Logger.getLogger(PaymentController.class.getName());

    @Value("${stripe.public.key}")
    private String publicKey;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SELLER')")
    @PostMapping("/create-checkout-session/{userId}")
    public Map<String, Object> createCheckoutSession(@PathVariable Long userId) throws Exception {
        CartResponseDTO cartResponse = cartService.getCartByUserId(userId);
        if (cartResponse.getItems() == null || cartResponse.getItems().isEmpty()) {
            throw new RuntimeException("Your cart is empty!");
        }
        Order order = orderService.createOrderFromCart(userId);


        SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success")
                .setCancelUrl("http://localhost:3000/payment-cancel")
                .putMetadata("orderId", order.getId().toString())
                .putMetadata("userId", userId.toString());

        for (var item : cartResponse.getItems()) {
            sessionBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(item.getQuantity().longValue())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("usd")
                                            .setUnitAmount((long) (item.getProductResponseDTO().getPrice() * 100))
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(item.getProductResponseDTO().getName())
                                                            .setDescription(item.getProductResponseDTO().getDescription())
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }

        Session session = Session.create(sessionBuilder.build());
        logger.info("Created Stripe checkout session with ID: " + session.getId());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", session.getId());
        responseData.put("publicKey", publicKey);
        responseData.put("url", session.getUrl());
        return responseData;
    }

    @PostMapping("/create-checkout-session/direct")
    public Map<String, Object> createDirectCheckoutSession(@RequestBody DirectCheckoutRequest request) throws Exception {
        // 1️⃣ Create a new order record in your database
        Order order = orderService.createDirectOrder(request);
        long amount = (long) (request.getPrice() * 100);

        // 2️⃣ Build Stripe Checkout parameters
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success")
                .setCancelUrl("http://localhost:3000/payment-cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(request.getQuantity().longValue())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(amount)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(request.getProductName())
                                                                .setDescription(request.getDescription())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                // ✅ Include orderId so webhook knows which order to mark as PAID
                .putMetadata("orderId", order.getId().toString())
                .putMetadata("userId", request.getUserId().toString())
                .putMetadata("productId", request.getProductId().toString())
                .build();

        // 3️⃣ Create the session on Stripe
        Session session = Session.create(params);
        logger.info("Created direct Stripe checkout session with ID: " + session.getId());

        // 4️⃣ Return data to frontend
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", session.getId());
        responseData.put("publicKey", publicKey);
        responseData.put("url", session.getUrl());
        return responseData;
    }
}
