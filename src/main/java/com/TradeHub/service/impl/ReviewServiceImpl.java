package com.TradeHub.service.impl;

import com.TradeHub.model.dto.ReviewDTO;
import com.TradeHub.model.entity.Product;
import com.TradeHub.model.entity.Review;
import com.TradeHub.model.entity.User;
import com.TradeHub.model.mapper.ReviewMapper;
import com.TradeHub.repository.ProductRepository;
import com.TradeHub.repository.ReviewRepository;
import com.TradeHub.repository.UserRepository;
import com.TradeHub.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewDTO addReview(ReviewDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        User reviewer = userRepository.findById(dto.getReviewerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Review review = reviewMapper.toEntity(dto);
        review.setProduct(product);
        review.setReviewer(reviewer);

        Review saved = reviewRepository.save(review);
        return reviewMapper.toDTO(saved);
    }

    @Override
    public List<ReviewDTO> getReviewsByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return reviewRepository.findByProduct(product)
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getReviewsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return reviewRepository.findByReviewer(user)
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO updateReview(Integer reviewId, Long userId, ReviewDTO dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        if (!review.getReviewer().getId().equals(userId)) {
            throw new SecurityException("You can only update your own reviews");
        }

        // Allow updating rating and comment only
        if (dto.getComment() != null && !dto.getComment().isBlank()) {
            review.setComment(dto.getComment());
        }
        if (dto.getRate() != null && dto.getRate() >= 1 && dto.getRate() <= 5) {
            review.setRate(dto.getRate());
        }

        Review updated = reviewRepository.save(review);
        return reviewMapper.toDTO(updated);
    }

    @Override
    public void deleteReview(Integer reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        if (!review.getReviewer().getId().equals(userId)) {
            throw new SecurityException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }
}
