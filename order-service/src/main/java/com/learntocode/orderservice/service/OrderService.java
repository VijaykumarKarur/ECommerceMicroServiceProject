package com.learntocode.orderservice.service;

import com.learntocode.orderservice.dto.OrderRequestDTO;
import com.learntocode.orderservice.dto.OrderResponseDTO;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO requestDTO);
    List<OrderResponseDTO> getAllOrders();
}
