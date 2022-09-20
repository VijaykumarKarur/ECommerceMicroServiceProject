package com.learntocode.inverntoryservice.service;

import com.learntocode.inverntoryservice.dto.InventoryRequestDTO;
import com.learntocode.inverntoryservice.dto.InventoryResponseDTO;
import com.learntocode.inverntoryservice.dto.InventoryUpdateRequestDTO;
import com.learntocode.inverntoryservice.dto.OrderInventoryDTO;
import com.learntocode.inverntoryservice.exception.InventoryNotFoundException;

import java.util.List;

public interface InventoryService {
    InventoryResponseDTO createInventory(InventoryRequestDTO requestDTO);
    List<InventoryResponseDTO> getAllInventories();
    InventoryResponseDTO getInventoryById(Long id) throws InventoryNotFoundException;
    InventoryResponseDTO getInventoryBySkuCodeIgnoreCase(String skuCode) throws InventoryNotFoundException;
    InventoryResponseDTO updateInventory(InventoryUpdateRequestDTO requestDTO) throws InventoryNotFoundException;
    void deleteInventoryById(Long id) throws InventoryNotFoundException;

    OrderInventoryDTO isInStock(OrderInventoryDTO requestDTO);
}
