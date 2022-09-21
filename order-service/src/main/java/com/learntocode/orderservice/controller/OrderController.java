package com.learntocode.orderservice.controller;

import com.learntocode.orderservice.dto.OrderRequestDTO;
import com.learntocode.orderservice.dto.OrderResponseDTO;
import com.learntocode.orderservice.exception.InventoryServiceCallException;
import com.learntocode.orderservice.exception.OrderNotFoundException;
import com.learntocode.orderservice.exception.ProductsNotInStockException;
import com.learntocode.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethodInventory")
    public OrderResponseDTO createOrder(@RequestBody OrderRequestDTO requestDTO) throws InventoryServiceCallException, ProductsNotInStockException {
        return orderService.createOrder(requestDTO);
    }

    public OrderResponseDTO fallbackMethodInventory(OrderRequestDTO requestDTO, Exception exception) throws InventoryServiceCallException {
        throw new InventoryServiceCallException("Inventory-Service Unavailable. Please try after sometime");
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

    /***
     * Endpoint to delete Order based on id
     * @param id of the Order to be deleted
     * @throws OrderNotFoundException is thrown if Order is not found
     */
    @DeleteMapping("/{id}")
    public void deleteOrderById(@PathVariable("id") Long id) throws OrderNotFoundException {
        orderService.deleteOrderById(id);
    }

    /***
     * Endpoint to delete Order based on orderNumber
     * @param orderNumber of the Order to be deleted
     * @throws OrderNotFoundException is thrown if Order is not found
     */
    @DeleteMapping("/orderNumber/{orderNumber}")
    public void deleteOrderByOrderNumber(@PathVariable("orderNumber") String orderNumber) throws OrderNotFoundException {
        orderService.deleteOrderByOrderNumber(orderNumber);
    }
}
