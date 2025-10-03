package com.TradeHub.controller;

import com.TradeHub.model.dto.ProductRequestDTO;
import com.TradeHub.model.dto.ProductResponseDTO;
import com.TradeHub.model.dto.ProductUpdateDTO;
import com.TradeHub.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productService.createProduct(productRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO productUpdateDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<ProductResponseDTO> uploadProductImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam Long sellerId) {
        return ResponseEntity.ok(productService.updateProductImage(id, file, sellerId));
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Void> deleteProductImage(
            @PathVariable Long id,
            @RequestParam Long sellerId) {
        productService.deleteProductImage(id, sellerId);
        return ResponseEntity.noContent().build();
    }
}
