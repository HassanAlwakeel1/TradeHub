package com.TradeHub.repository;

import com.TradeHub.model.entity.Cart;
import com.TradeHub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
