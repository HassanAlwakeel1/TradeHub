package com.TradeHub.controller;

import com.TradeHub.model.dto.CartItemRequestDTO;
import com.TradeHub.model.dto.CartResponseDTO;
import com.TradeHub.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 1️⃣ Get cart by user id
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    // 2️⃣ Add item to cart
    @PostMapping("/{userId}/items")
    public ResponseEntity<CartResponseDTO> addItemToCart(
            @PathVariable Long userId,
            @RequestBody CartItemRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(cartService.addItemToCart(userId, requestDTO));
    }

    // 3️⃣ Update item quantity in cart
    @PutMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponseDTO> updateItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, productId, quantity));
    }

    // 4️⃣ Remove item from cart
    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponseDTO> removeItem(
            @PathVariable Long userId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, productId));
    }

    // 5️⃣ Clear cart
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<CartResponseDTO> clearCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }
}
