package com.learntocode.productservice.service;

import com.learntocode.productservice.dto.ProductRequestDTO;
import com.learntocode.productservice.dto.ProductResponseDTO;
import com.learntocode.productservice.dto.ProductUpdateRequestDTO;
import com.learntocode.productservice.exception.ProductNotFoundException;
import com.learntocode.productservice.model.Product;
import com.learntocode.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    /***
     * Method to create Product
     * @param requestDTO contains details needed to create Product
     * @return ProductResponseDTO with saved Product details
     */
    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        Product product = mapProductRequestToProduct(requestDTO);
        product = productRepository.save(product);
        return mapProductToProductResponse(product);
    }

    /***
     * Method to get all Products
     * @return List of ProductResponseDTO containing all Products
     */
    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(this::mapProductToProductResponse).toList();
    }

    /***
     * Method to get Product based on id
     * @param id id of the Product
     * @return ProductResponseDTO containing the Product details of given Product id
     * @throws ProductNotFoundException is thrown if Product is not found
     */
    @Override
    public ProductResponseDTO getProductById(String id) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findById(id);
        if(!product.isEmpty()){
            throw new ProductNotFoundException("Product "+ id +" Not Found");
        }
        return mapProductToProductResponse(product.get());
    }

    /***
     * Method to update Product
     * @param requestDTO contains the Product details that need to be updated
     * @return ProductResponseDTO containing the updated Product details
     * @throws ProductNotFoundException is thrown if Product is not found
     */
    @Override
    public ProductResponseDTO updateProduct(ProductUpdateRequestDTO requestDTO) throws ProductNotFoundException {
        Optional<Product> productOptional = productRepository.findById(requestDTO.getId());
        if(!productOptional.isEmpty()){
            throw new ProductNotFoundException("Product "+ requestDTO.getId() +" Not Found");
        }
        Product product = productOptional.get();
        if(Objects.nonNull(product.getName()) &&
        !"".equalsIgnoreCase(product.getName())){
            product.setName(product.getName());
        }
        if(Objects.nonNull(product.getDescription()) &&
        !"".equalsIgnoreCase(product.getDescription())){
            product.setDescription(product.getDescription());
        }
        if(Objects.nonNull(product.getPrice()) &&
        !"".equalsIgnoreCase(product.getPrice().toString())){
            product.setPrice(product.getPrice());
        }
        return mapProductToProductResponse(productRepository.save(product));
    }

    /***
     * Method to delete Product given by id
     * @param id of the Product
     * @throws ProductNotFoundException is thrown if Product not found
     */
    @Override
    public void deleteProduct(String id) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            throw new ProductNotFoundException("Product "+ id + " Not Found");
        }
        productRepository.deleteById(id);
    }

    /***
     * Method to map ProductRequestDTO to Product
     * @param requestDTO ProductRequestDTO to be mapped
     * @return Product
     */
    private Product mapProductRequestToProduct(ProductRequestDTO requestDTO){
        return Product
                .builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .price(requestDTO.getPrice())
                .build();
    }

    /***
     * Helper method to map Product to ProductResponseDTO
     * @param product Product to be mapped
     * @return ProductResponseDTO
     */
    private ProductResponseDTO mapProductToProductResponse(Product product) {
        return ProductResponseDTO
                .builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
