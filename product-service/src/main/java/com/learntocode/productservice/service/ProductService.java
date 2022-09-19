package com.learntocode.productservice.service;

import com.learntocode.productservice.dto.ProductRequestDTO;
import com.learntocode.productservice.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO requestDTO);
    List<ProductResponseDTO> getAllProducts();
}
