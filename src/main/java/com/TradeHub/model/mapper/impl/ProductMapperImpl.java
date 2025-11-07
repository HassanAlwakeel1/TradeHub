package com.TradeHub.model.mapper.impl;

import com.TradeHub.model.dto.ProductRequestDTO;
import com.TradeHub.model.dto.ProductResponseDTO;
import com.TradeHub.model.dto.ReviewDTO;
import com.TradeHub.model.entity.Product;
import com.TradeHub.model.entity.Review;
import com.TradeHub.model.mapper.ProductMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapperImpl implements ProductMapper {

    private final ModelMapper modelMapper;

    public ProductMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Product toEntity(ProductRequestDTO dto) {
        return modelMapper.map(dto, Product.class);
    }

    @Override
    public ProductResponseDTO toResponseDto(Product product) {
        ProductResponseDTO dto = modelMapper.map(product, ProductResponseDTO.class);

        // Map seller info safely
        if (product.getSeller() != null) {
            dto.setSellerId(product.getSeller().getId());
        }

        // Map reviews
        List<Review> reviews = product.getReviews();
        if (reviews != null && !reviews.isEmpty()) {
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(review -> {
                        ReviewDTO rDto = new ReviewDTO();
                        rDto.setId(review.getId());
                        rDto.setRate(review.getRate());
                        rDto.setComment(review.getComment());
                        rDto.setReviewerId(review.getReviewer() != null ? review.getReviewer().getId() : null);
                        rDto.setProductId(product.getId());
                        return rDto;
                    })
                    .collect(Collectors.toList());
            dto.setReviews(reviewDTOs);
        }

        return dto;
    }
}