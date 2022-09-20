package com.learntocode.orderservice.service;

import com.learntocode.orderservice.dto.OrderLineItemDTO;
import com.learntocode.orderservice.dto.OrderRequestDTO;
import com.learntocode.orderservice.dto.OrderResponseDTO;
import com.learntocode.orderservice.model.Order;
import com.learntocode.orderservice.model.OrderLineItem;
import com.learntocode.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;

    /***
     * Method to save Order along with OrderLineItems specified in the Order
     * @param requestDTO Order request
     * @return OrderResponseDTO containing Order and OrderLineItems saved into the system
     */
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO){
        Order order = mapOrderRequestDTOToOrder(requestDTO);
        order = orderRepository.save(order);
        return mapOrderToOrderResponseDTO(order);
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
