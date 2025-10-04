package com.TradeHub.service.impl;

import com.TradeHub.model.dto.CartItemRequestDTO;
import com.TradeHub.model.dto.CartResponseDTO;
import com.TradeHub.model.entity.Cart;
import com.TradeHub.model.entity.CartItem;
import com.TradeHub.model.entity.Product;
import com.TradeHub.model.entity.User;
import com.TradeHub.model.mapper.CartMapper;
import com.TradeHub.repository.CartItemRepository;
import com.TradeHub.repository.CartRepository;
import com.TradeHub.repository.ProductRepository;
import com.TradeHub.repository.UserRepository;
import com.TradeHub.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartServiceImpl(
            CartRepository cartRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            CartMapper cartMapper
    ) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public CartResponseDTO getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        return cartMapper.toResponseDTO(cart);
    }

    @Override
    public CartResponseDTO clearCart(Long userId) {
        Cart cart = getCartEntity(userId);
        cart.getCartItemList().clear();
        cartRepository.saveAndFlush(cart); // ensure DB sync immediately
        return cartMapper.toResponseDTO(cart);
    }

    @Override
    public CartResponseDTO addItemToCart(Long userId, CartItemRequestDTO requestDTO) {
        Cart cart = getCartEntity(userId);

        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cart.getCartItemList().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + requestDTO.getQuantity());
            item.setPrice(item.getQuantity() * product.getPrice());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(requestDTO.getQuantity());
            newItem.setPrice(product.getPrice() * requestDTO.getQuantity());
            cart.getCartItemList().add(newItem);
        }

        cartRepository.save(cart);
        return cartMapper.toResponseDTO(cart);
    }

    @Override
    public CartResponseDTO removeItemFromCart(Long userId, Long productId) {
        Cart cart = getCartEntity(userId);

        cart.getCartItemList().removeIf(item -> item.getProduct().getId().equals(productId));

        cartRepository.save(cart);
        return cartMapper.toResponseDTO(cart);
    }

    @Override
    public CartResponseDTO updateItemQuantity(Long userId, Long productId, Integer quantity) {
        Cart cart = getCartEntity(userId);

        CartItem item = cart.getCartItemList().stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        if (quantity <= 0) {
            cart.getCartItemList().remove(item);
        } else {
            item.setQuantity(quantity);
            item.setPrice(productRepository.findById(productId)
                    .map(Product::getPrice)
                    .orElseThrow(() -> new RuntimeException("Product not found")) * quantity);
        }

        cartRepository.save(cart);
        return cartMapper.toResponseDTO(cart);
    }

    // ✅ helper method to fetch or create cart
    private Cart getCartEntity(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }
}
