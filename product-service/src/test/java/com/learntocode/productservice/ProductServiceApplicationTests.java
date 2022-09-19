package com.learntocode.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.learntocode.productservice.dto.ProductRequestDTO;
import com.learntocode.productservice.dto.ProductResponseDTO;
import com.learntocode.productservice.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ProductServiceApplicationTests {
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    /***
     * Integration Test of POST /api/products/ api endpoint. A product is saved to MongoDb container
     * and response is verified.
     * @throws Exception ObjectMapper read and write methods, and MockMVC perform methods throw exception
     */
    @Test
    @DisplayName("Integration Test01 - Create Product")
    public void shouldCreateProduct() throws Exception {
        //creation api request
        String productRequestString = objectMapper.writeValueAsString(
                getProductRequest("Samsung F51","Samsung Phone", BigDecimal.valueOf(33500)));

        //calling post method and asserting response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequestString))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("IPhone 13"));
    }

    /***
     * Integration Test of GET /api/products/ api endpoint. Products are saved to MongoDb container using
     * POST /api/products/ api and then GET /api/products/ is performed to verify the result.
     * @throws Exception ObjectMapper read and write methods, and MockMVC perform methods throw exception
     */
    @Test
    @DisplayName("Integration Test02 - Get All Products")
    public void shouldGetAllProducts() throws Exception{
        //Products are created using POST
        String productRequestString1 = objectMapper.writeValueAsString(
                getProductRequest("Nike 2154","Nike Shoes", BigDecimal.valueOf(6500)));
        String productRequestString2 = objectMapper.writeValueAsString(
                getProductRequest("IPhone 13","Apple IPhone", BigDecimal.valueOf(63500)));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(productRequestString1))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString2))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //GET on /api/products/ to obtain all the products and asserting the response
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/"))
                .andReturn();
        List<ProductResponseDTO> productList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                TypeFactory.defaultInstance().constructCollectionType(List.class, ProductResponseDTO.class));
        assertEquals(productRepository.findAll().size(), productList.size());
    }

    /***
     * Helper method to build ProductRequestDTO
     * @return Method return ProductRequestDTO built using parameters
     */
    public ProductRequestDTO getProductRequest(String name, String description, BigDecimal price){
        return ProductRequestDTO
                .builder()
                .name(name)
                .description(description)
                .price(price)
                .build();
    }
}
