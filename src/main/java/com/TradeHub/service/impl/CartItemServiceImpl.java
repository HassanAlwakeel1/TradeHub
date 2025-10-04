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
import com.TradeHub.service.CartItemService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartItemServiceImpl(CartRepository cartRepository,
                               CartItemRepository cartItemRepository,
                               ProductRepository productRepository,
                               UserRepository userRepository,
                               CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public CartResponseDTO addItemToCart(Long userId, CartItemRequestDTO requestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + requestDTO.getQuantity());
        cartItem.setPrice(product.getPrice() * cartItem.getQuantity());

        cartItemRepository.save(cartItem);

        return cartMapper.toResponseDTO(cart);
    }

    @Override
    public CartResponseDTO updateItemQuantity(Long userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItem.setQuantity(quantity);
        cartItem.setPrice(product.getPrice() * quantity);

        cartItemRepository.save(cartItem);

        return cartMapper.toResponseDTO(cart);
    }

    @Override
    public CartResponseDTO removeItemFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItemRepository.delete(cartItem);

        return cartMapper.toResponseDTO(cart);
    }
}
