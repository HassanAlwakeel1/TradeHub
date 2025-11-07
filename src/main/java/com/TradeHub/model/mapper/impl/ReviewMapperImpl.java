package com.TradeHub.model.mapper.impl;

import com.TradeHub.model.dto.ReviewDTO;
import com.TradeHub.model.entity.Review;
import com.TradeHub.model.mapper.ReviewMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewMapperImpl implements ReviewMapper {
    private final ModelMapper modelMapper;

    public ReviewMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ReviewDTO toDTO(Review review) {
        return modelMapper.map(review, ReviewDTO.class);
    }

    @Override
    public Review toEntity(ReviewDTO dto) {
        return modelMapper.map(dto, Review.class);
    }

    @Override
    public List<ReviewDTO> toDTOList(List<Review> reviews) {
        return reviews.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

}
