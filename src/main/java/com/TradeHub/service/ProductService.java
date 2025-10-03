package com.TradeHub.service;


import com.TradeHub.model.dto.ProductRequestDTO;
import com.TradeHub.model.dto.ProductResponseDTO;
import com.TradeHub.model.dto.ProductUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
    ProductResponseDTO updateProduct(Long id, ProductUpdateDTO productUpdateDTO);
    void deleteProduct(Long id);
    ProductResponseDTO getProductById(Long id);
    List<ProductResponseDTO> getAllProducts();
    List<ProductResponseDTO> getProductsByCategory(String category);
    List<ProductResponseDTO> searchProducts(String keyword);

    ProductResponseDTO updateProductImage(Long productId, MultipartFile file, Long sellerId);
    void deleteProductImage(Long productId, Long sellerId);
}
