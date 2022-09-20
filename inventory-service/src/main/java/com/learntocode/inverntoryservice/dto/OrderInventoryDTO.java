package com.learntocode.inverntoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInventoryDTO {
    private List<OrderLineItemInventoryDTO> orderLineItemInventoryDTOList;
}
