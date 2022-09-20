package com.learntocode.orderservice.controller;

import com.learntocode.orderservice.dto.OrderRequestDTO;
import com.learntocode.orderservice.dto.OrderResponseDTO;
import com.learntocode.orderservice.exception.OrderNotFoundException;
import com.learntocode.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /***
     * Endpoint to get all Orders in the system
     * @return List of OrderResponseDTO
     */
    @GetMapping("/")
    public List<OrderResponseDTO> getAllOrders(){
        return orderService.getAllOrders();
    }

    /***
     * Endpoint to get Order by id
     * @param id of the Order to be retrieved
     * @return OrderResponseDTO containing the details of Order retrieved
     * @throws OrderNotFoundException is thrown if Order is not found
     */
    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable("id") Long id) throws OrderNotFoundException {
        return orderService.getOrderById(id);
    }

    /***
     * Endpoint to get Order based on orderNumber
     * @param orderNumber of the Order to be retrieved
     * @return OrderResponseDTO containing the details of retrieved Order
     * @throws OrderNotFoundException is thrown if Order is not found
     */
    @GetMapping("/orderNumber/{orderNumber}")
    public OrderResponseDTO getOrderByOrderNumber(@PathVariable("orderNumber") String orderNumber) throws OrderNotFoundException {
        return orderService.getOrderByOrderNumber(orderNumber);
    }
}
