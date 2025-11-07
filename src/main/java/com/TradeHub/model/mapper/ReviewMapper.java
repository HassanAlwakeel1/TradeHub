package com.TradeHub.model.mapper;

import com.TradeHub.model.dto.ReviewDTO;
import com.TradeHub.model.entity.Review;

import java.util.List;

public interface ReviewMapper {
    ReviewDTO toDTO(Review review);
    Review toEntity(ReviewDTO dto);
    List<ReviewDTO> toDTOList(List<Review> reviews);
}
