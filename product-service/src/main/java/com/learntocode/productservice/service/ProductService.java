package com.learntocode.productservice.service;

import com.learntocode.productservice.dto.ProductRequestDTO;
import com.learntocode.productservice.dto.ProductResponseDTO;
import com.learntocode.productservice.dto.ProductUpdateRequestDTO;
import com.learntocode.productservice.exception.ProductNotFoundException;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO requestDTO);
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO getProductById(String id) throws ProductNotFoundException;
    ProductResponseDTO updateProduct(ProductUpdateRequestDTO requestDTO) throws ProductNotFoundException;
    void deleteProduct(String id) throws ProductNotFoundException;
}
