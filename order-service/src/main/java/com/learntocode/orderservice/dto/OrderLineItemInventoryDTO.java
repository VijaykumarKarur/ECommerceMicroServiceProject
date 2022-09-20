package com.learntocode.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItemInventoryDTO {
    private String skuCode;
    private Integer requiredQuantity;
    private Integer availableQuantity;
    private Boolean inStock;
}
