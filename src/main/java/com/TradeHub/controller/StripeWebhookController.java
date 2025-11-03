package com.TradeHub.controller;

import com.TradeHub.model.entity.Order;
import com.TradeHub.model.entity.enums.OrderStatus;
import com.TradeHub.repository.OrderRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@Tag(name = "Stripe Webhook")
@CrossOrigin
@RequiredArgsConstructor
public class StripeWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);

    private final OrderRepository orderRepository;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            logger.info("✅ Received Stripe event: {}", event.getType());
        } catch (SignatureVerificationException e) {
            logger.warn("⚠️ Stripe signature verification failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Signature verification failed");
        }

        try {
            switch (event.getType()) {
                case "checkout.session.completed" -> handleCheckoutSessionCompleted(event);
                default -> logger.info("ℹ️ Unhandled event type: {}", event.getType());
            }
        } catch (Exception e) {
            logger.error("❌ Error handling event {}", event.getType(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error handling event");
        }

        return ResponseEntity.ok("Webhook received");
    }

    /**
     * Handle checkout.session.completed event.
     * If the session can't be deserialized by Stripe's SDK, we fall back to manual JSON parsing.
     */
    private void handleCheckoutSessionCompleted(Event event) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();

        Session session = null;
        if (deserializer.getObject().isPresent()) {
            session = (Session) deserializer.getObject().get();
        } else {
            // ✅ Fallback: manually parse JSON if deserializer fails
            logger.warn("⚠️ Could not deserialize session object, attempting manual parse");
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(event.getData().getObject().toJson());
                JsonNode metadata = root.get("metadata");

                if (metadata != null && metadata.has("orderId")) {
                    String orderId = metadata.get("orderId").asText();
                    Long amountTotal = root.has("amount_total") ? root.get("amount_total").asLong() : 0L;
                    markOrderAsPaid(orderId, amountTotal);
                    return;
                } else {
                    logger.warn("⚠️ No orderId found in metadata JSON");
                    return;
                }
            } catch (Exception e) {
                logger.error("❌ Error parsing fallback JSON: {}", e.getMessage());
                return;
            }
        }

        if (session == null) {
            logger.warn("⚠️ Session was null");
            return;
        }

        String orderId = session.getMetadata().get("orderId");
        if (orderId == null) {
            logger.warn("⚠️ No orderId in session metadata");
            return;
        }

        markOrderAsPaid(orderId, session.getAmountTotal());
    }

    /**
     * Update order status in DB to PAID.
     */
    private void markOrderAsPaid(String orderId, Long amountTotal) {
        orderRepository.findById(Long.parseLong(orderId)).ifPresentOrElse(order -> {
            if (order.getStatus() != OrderStatus.PAID) {
                double paidAmount = (amountTotal != null ? amountTotal / 100.0 : 0.0);
                order.setTotalPrice(paidAmount);
                order.setStatus(OrderStatus.PAID);
                orderRepository.save(order);
                logger.info("✅ Order {} marked as PAID with amount ${}", orderId, paidAmount);
            } else {
                logger.info("ℹ️ Order {} already marked as PAID", orderId);
            }
        }, () -> logger.warn("⚠️ Order with ID {} not found", orderId));
    }
}