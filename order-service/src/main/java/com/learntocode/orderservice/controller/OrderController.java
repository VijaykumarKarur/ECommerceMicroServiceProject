package com.learntocode.orderservice.controller;

import com.learntocode.orderservice.dto.OrderRequestDTO;
import com.learntocode.orderservice.dto.OrderResponseDTO;
import com.learntocode.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /***
     * Endpoint to create Order and corresponding OrderLineItems contained within
     * @param requestDTO Order create request
     * @return OrderResponseDTO containing Order and OrderLineItems saved in the system
     */
    @PostMapping("/")
    public OrderResponseDTO createOrder(@RequestBody OrderRequestDTO requestDTO){
        return orderService.createOrder(requestDTO);
    }
}
