package com.learntocode.productservice.controller;

import com.learntocode.productservice.dto.ProductRequestDTO;
import com.learntocode.productservice.dto.ProductResponseDTO;
import com.learntocode.productservice.dto.ProductUpdateRequestDTO;
import com.learntocode.productservice.exception.ProductNotFoundException;
import com.learntocode.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /***
     * Endpoint to create Product
     * @param requestDTO contains Product details to be saved
     * @return ProductResponseDTO contains the saved Product details
     */
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO createProduct(@RequestBody ProductRequestDTO requestDTO){
        return productService.createProduct(requestDTO);
    }

    /***
     * Endpoint to get list of all Products
     * @return List of ProductResponseDTO containing all products
     */
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDTO> getAllProducts(){
        return productService.getAllProducts();
    }

    /***
     * Endpoint to get Product based on id
     * @param id of the Product
     * @return ProductResponseDTO containing the Product details
     * @throws ProductNotFoundException is thrown if Product is not found
     */
    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable("id") String id) throws ProductNotFoundException {
        return productService.getProductById(id);
    }

    /***
     * Endpoint to update Product details
     * @param requestDTO containing the Product details that need to be updated
     * @return ProductResponseDTO containing the Product details
     * @throws ProductNotFoundException is thrown if Product is not found
     */
    @PatchMapping("/")
    public ProductResponseDTO updateProduct(@RequestBody ProductUpdateRequestDTO requestDTO) throws ProductNotFoundException {
        return productService.updateProduct(requestDTO);
    }

    /***
     * Endpoint to delete Product
     * @param id of the Product to be deleted
     * @throws ProductNotFoundException is thrown if Product is not found
     */
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") String id) throws ProductNotFoundException {
        productService.deleteProduct(id);
    }
}
