package com.TradeHub.service;

import com.TradeHub.model.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    ReviewDTO addReview(ReviewDTO dto);
    List<ReviewDTO> getReviewsByProduct(Long productId);
    List<ReviewDTO> getReviewsByUser(Long userId);
    ReviewDTO updateReview(Integer reviewId, Long userId, ReviewDTO dto);
    void deleteReview(Integer reviewId, Long userId);
}
