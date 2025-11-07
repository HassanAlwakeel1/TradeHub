package com.TradeHub.service.impl;

import com.TradeHub.model.dto.ProductRequestDTO;
import com.TradeHub.model.dto.ProductResponseDTO;
import com.TradeHub.model.dto.ProductUpdateDTO;
import com.TradeHub.model.entity.Product;
import com.TradeHub.model.entity.User;
import com.TradeHub.model.mapper.ProductMapper;
import com.TradeHub.repository.ProductRepository;
import com.TradeHub.repository.UserRepository;
import com.TradeHub.service.CloudinaryImageService;
import com.TradeHub.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CloudinaryImageService cloudinaryImageService;
    private final UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductMapper productMapper,
                              CloudinaryImageService cloudinaryImageService,
                              UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.cloudinaryImageService = cloudinaryImageService;
        this.userRepository = userRepository;
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        // 1. Find seller by ID
        User seller = userRepository.findById(productRequestDTO.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found with ID: " + productRequestDTO.getSellerId()));

        // 2. Map DTO to entity
        Product product = productMapper.toEntity(productRequestDTO);

        // 3. Set seller explicitly
        product.setSeller(seller);
        product.setId(null);

        // 4. Save product
        return productMapper.toResponseDto(productRepository.save(product));
    }



    @Override
    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO productUpdateDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // update fields
        product.setName(productUpdateDTO.getName());
        product.setDescription(productUpdateDTO.getDescription());
        product.setPrice(productUpdateDTO.getPrice());
        product.setCategory(productUpdateDTO.getCategory());
        product.setStockQuantity(productUpdateDTO.getStockQuantity());

        return productMapper.toResponseDto(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findByIdWithReviews(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toResponseDto(product);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAllWithReviews()
                .stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategory(String category) {
        return productRepository.findByCategory(category)
                .stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO updateProductImage(Long productId, MultipartFile file, Long sellerId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized to update this product");
        }

        // Upload image to Cloudinary
        Map<String, Object> uploadResult = cloudinaryImageService.upload(file);
        String imageUrl = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id"); // store this for deletion later

        product.setImageUrl(imageUrl);
        product.setImagePublicId(publicId); // add this field in Product entity

        return productMapper.toResponseDto(productRepository.save(product));
    }

    @Override
    public void deleteProductImage(Long productId, Long sellerId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized to delete image");
        }

        if (product.getImagePublicId() != null) {
            try {
                cloudinaryImageService.delete(product.getImagePublicId()); // implement delete
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete image from Cloudinary", e);
            }
            product.setImageUrl(null);
            product.setImagePublicId(null);
            productRepository.save(product);
        }
    }
}
