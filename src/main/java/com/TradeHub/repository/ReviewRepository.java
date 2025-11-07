package com.TradeHub.repository;

import com.TradeHub.model.entity.Product;
import com.TradeHub.model.entity.Review;
import com.TradeHub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProduct(Product product);
    List<Review> findByReviewer(User reviewer);
}
