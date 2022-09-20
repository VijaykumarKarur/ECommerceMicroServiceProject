package com.learntocode.orderservice.exception;

import com.learntocode.orderservice.dto.OrderInventoryDTO;
import lombok.Data;

@Data
public class ProductsNotInStockException extends Exception{
    private OrderInventoryDTO orderInventoryDTO;
    public ProductsNotInStockException() {
        super();
    }

    public ProductsNotInStockException(String message) {
        super(message);
    }

    public ProductsNotInStockException(String message, OrderInventoryDTO orderInventoryDTO) {
        super(message);
        this.orderInventoryDTO = orderInventoryDTO;
    }
}
