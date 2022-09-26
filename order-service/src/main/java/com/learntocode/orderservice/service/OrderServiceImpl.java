package com.learntocode.orderservice.service;

import com.learntocode.orderservice.dto.*;
import com.learntocode.orderservice.event.OrderPlacedEvent;
import com.learntocode.orderservice.exception.InventoryServiceCallException;
import com.learntocode.orderservice.exception.OrderNotFoundException;
import com.learntocode.orderservice.exception.ProductsNotInStockException;
import com.learntocode.orderservice.model.Order;
import com.learntocode.orderservice.model.OrderLineItem;
import com.learntocode.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    /***
     * Method to save Order along with OrderLineItems specified in the Order
     * Firstly, products and their quantity are verified through inventory-service for availability
     * before Order is being created
     * @param requestDTO Order request
     * @return OrderResponseDTO containing Order and OrderLineItems saved into the system
     */
    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO)
            throws InventoryServiceCallException, ProductsNotInStockException {
        OrderInventoryDTO responseDTO;
        try{
            /*
             Need to verify if the requested products and their required quantity are available in stock.
            An inventory-service call is needed to check the stock.
            */
            OrderInventoryDTO orderInventoryDTO = new OrderInventoryDTO();
            List<OrderLineItemInventoryDTO> orderLineItemInventoryDTOList =
                    requestDTO
                            .getOrderLineItemList()
                            .stream()
                            .map(this::mapOrderLineItemDTOToOrderLineItemInventoryDTO)
                            .toList();
            orderInventoryDTO.setOrderLineItemInventoryDTOList(orderLineItemInventoryDTOList);
            WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec =
                    (WebClient.UriSpec<WebClient.RequestBodySpec>) webClientBuilder.build().get();
            WebClient.RequestBodySpec bodySpec = uriSpec.uri("lb://inventory-service/api/inventories/orderInStock/");
            WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(orderInventoryDTO);
            responseDTO = headersSpec
                    .retrieve()
                    .bodyToMono(OrderInventoryDTO.class)
                    .block();
        }
        catch(Exception exception){
            throw new InventoryServiceCallException("Inventory Service Call Exception");
        }
        boolean allInStock = responseDTO
                .getOrderLineItemInventoryDTOList()
                .stream()
                .allMatch(orderLineItemInventoryDTO -> orderLineItemInventoryDTO.getInStock());
        if(allInStock){
            Order order = mapOrderRequestDTOToOrder(requestDTO);
            order = orderRepository.save(order);
            kafkaTemplate.send("OrderPlacedTopic",
                    OrderPlacedEvent.builder().orderNumber(order.getOrderNumber()).build());
            return mapOrderToOrderResponseDTO(order);
        }
        else{
            throw new ProductsNotInStockException("Products Not in Stock", responseDTO);
        }
    }

    /***
     * Helper method to map OrderLineItemDTO to OrderLineItemInventoryDTO object
     * @param requestDTO OrderLineItemDTO to be mapped
     * @return OrderLineItemInventoryDTO used in inventory-service call
     */
    public OrderLineItemInventoryDTO mapOrderLineItemDTOToOrderLineItemInventoryDTO(OrderLineItemDTO requestDTO){
        return OrderLineItemInventoryDTO
                .builder()
                .skuCode(requestDTO.getSkuCode())
                .requiredQuantity(requestDTO.getQuantity())
                .build();
    }


    /***
     * Method to get all Orders in the system
     * @return List of OrderResponseDTO containing all the Orders
     */
    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        return orderList
                .stream()
                .map(this::mapOrderToOrderResponseDTO)
                .toList();
    }

    /***
     * Method to get Order based on id
     * @param id of the Order to be retrieved
     * @return OrderResponseDTO containing the details of Order retrieved
     * @throws OrderNotFoundException is thrown if Order not found
     */
    @Override
    public OrderResponseDTO getOrderById(Long id) throws OrderNotFoundException {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isEmpty()){
            throw new OrderNotFoundException("Order "+ id + " Not Found");
        }
        return mapOrderToOrderResponseDTO(orderOptional.get());
    }

    /***
     * Method to get Order based on orderNumber
     * @param orderNumber of Order to be retrieved
     * @return OrderResponseDTO containing the retrieved Order details
     * @throws OrderNotFoundException is thrown if Order is not found
     */
    @Override
    public OrderResponseDTO getOrderByOrderNumber(String orderNumber) throws OrderNotFoundException {
        Optional<Order> orderOptional = orderRepository.findOrderByOrderNumberIgnoreCase(orderNumber);
        if(orderOptional.isEmpty()){
            throw new OrderNotFoundException("Order "+ orderNumber + " Not Found");
        }
        return mapOrderToOrderResponseDTO(orderOptional.get());
    }

    /***
     * Method to delete Order by id
     * @param id of the Order to be deleted
     * @throws OrderNotFoundException is thrown if Order not found
     */
    @Override
    public void deleteOrderById(Long id) throws OrderNotFoundException {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isEmpty()){
            throw new OrderNotFoundException("Order "+ id + " Not Found");
        }
        orderRepository.deleteById(id);
    }

    /***
     * Method to delete Order based on Order Number
     * @param orderNumber of the Order to be deleted
     * @throws OrderNotFoundException is thrown if Order not found
     */
    @Override
    public void deleteOrderByOrderNumber(String orderNumber) throws OrderNotFoundException {
        Optional<Order> orderOptional = orderRepository.findOrderByOrderNumberIgnoreCase(orderNumber);
        if(orderOptional.isEmpty()){
            throw new OrderNotFoundException("Order "+ orderNumber + " Not Found");
        }
        orderRepository.deleteById(orderOptional.get().getId());
    }

    /***
     * Helper method to map OrderRequestDTO to Order
     * @param requestDTO OrderRequestDTO
     * @return Order
     */
    public Order mapOrderRequestDTOToOrder(OrderRequestDTO requestDTO){
        List<OrderLineItem> orderLineItemList =
                requestDTO
                        .getOrderLineItemList()
                        .stream()
                        .map(this::mapOrderLineItemDTOToOrderLineItem)
                        .toList();
        return Order
                .builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemList(orderLineItemList)
                .build();
    }

    /***
     * Helper method to map OrderLineItemDTO to OrderLineItem
     * @param requestDTO OrderLineItemDTO
     * @return OrderLineItem
     */
    public OrderLineItem mapOrderLineItemDTOToOrderLineItem(OrderLineItemDTO requestDTO){
        return OrderLineItem
                .builder()
                .skuCode(requestDTO.getSkuCode())
                .price(requestDTO.getPrice())
                .quantity(requestDTO.getQuantity())
                .build();
    }

    /***
     * Helper method to map Order to OrderResponseDTO
     * @param order Order to be mapped
     * @return OrderResponseDTO
     */
    public OrderResponseDTO mapOrderToOrderResponseDTO(Order order){
        List<OrderLineItemDTO> orderLineItemDTOList = order
                .getOrderLineItemList()
                .stream()
                .map(this::mapOrderLineItemToOrderLineItemDTO)
                .toList();

        return OrderResponseDTO
                .builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderLineItemList(orderLineItemDTOList)
                .build();
    }

    /***
     * Helper method to map OrderLineItem to OrderLineItemDTO
     * @param orderLineItem OrderLineItem to be mapped
     * @return OrderLineItemDTO
     */
    public OrderLineItemDTO mapOrderLineItemToOrderLineItemDTO(OrderLineItem orderLineItem){
        return OrderLineItemDTO
                .builder()
                .id(orderLineItem.getId())
                .skuCode(orderLineItem.getSkuCode())
                .price(orderLineItem.getPrice())
                .quantity(orderLineItem.getQuantity())
                .build();
    }
}
