package com.learntocode.orderservice.service;

import com.learntocode.orderservice.dto.OrderRequestDTO;
import com.learntocode.orderservice.dto.OrderResponseDTO;
import com.learntocode.orderservice.exception.InventoryServiceCallException;
import com.learntocode.orderservice.exception.OrderNotFoundException;
import com.learntocode.orderservice.exception.ProductsNotInStockException;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO requestDTO) throws InventoryServiceCallException, ProductsNotInStockException;
    List<OrderResponseDTO> getAllOrders();
    OrderResponseDTO getOrderById(Long id) throws OrderNotFoundException;
    OrderResponseDTO getOrderByOrderNumber(String orderNumber) throws OrderNotFoundException;
    void deleteOrderById(Long id) throws OrderNotFoundException;
    void deleteOrderByOrderNumber(String orderNumber) throws OrderNotFoundException;
}
