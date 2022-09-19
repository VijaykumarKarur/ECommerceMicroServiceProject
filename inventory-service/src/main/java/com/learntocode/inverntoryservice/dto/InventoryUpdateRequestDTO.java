package com.learntocode.inverntoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUpdateRequestDTO {
    private Long id;
    private String skuCode;
    private Integer quantity;
}
