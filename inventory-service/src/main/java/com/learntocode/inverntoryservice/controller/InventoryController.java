package com.learntocode.inverntoryservice.controller;

import com.learntocode.inverntoryservice.dto.InventoryRequestDTO;
import com.learntocode.inverntoryservice.dto.InventoryResponseDTO;
import com.learntocode.inverntoryservice.dto.InventoryUpdateRequestDTO;
import com.learntocode.inverntoryservice.dto.OrderInventoryDTO;
import com.learntocode.inverntoryservice.exception.InventoryNotFoundException;
import com.learntocode.inverntoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    /***
     * Endpoint to create inventory
     * @param requestDTO contains skuCode and quantity
     * @return InventoryResponseDTO contains id, skuCode and quantity
     */
    @PostMapping("/")
    public InventoryResponseDTO createInventory(@RequestBody InventoryRequestDTO requestDTO){
        return inventoryService.createInventory(requestDTO);
    }

    /***
     * Endpoint to get all Inventories
     * @return list of InventoryResponseDTO
     */
    @GetMapping("/")
    public List<InventoryResponseDTO> getAllInventories(){
        return inventoryService.getAllInventories();
    }

    /***
     * Endpoint to get an Inventory based on id
     * @param id of an Inventory
     * @return InventoryResponseDTO containing the details of an Inventory
     * @throws InventoryNotFoundException is thrown when Inventory is not found
     */
    @GetMapping("/{id}")
    public InventoryResponseDTO getInventoryById(@PathVariable("id") Long id) throws InventoryNotFoundException {
        return inventoryService.getInventoryById(id);
    }

    /***
     * Endpoint to get an Inventory based on skuCode
     * @param skuCode of Inventory
     * @return InventoryResponseDTO containing the details of an Inventory
     * @throws InventoryNotFoundException is thrown when Inventory is not found
     */
    @GetMapping("/skuCode/{skuCode}")
    public InventoryResponseDTO getInventoryBySkuCodeIgnoreCase(@PathVariable String skuCode) throws InventoryNotFoundException {
        return inventoryService.getInventoryBySkuCodeIgnoreCase(skuCode);
    }

    /***
     * Endpoint to update an Inventory
     * @param requestDTO contains the details of Inventory to be updated
     * @return InventoryResponseDTO containing the details of an Inventory
     * @throws InventoryNotFoundException is thrown when Inventory is not found
     */
    @PatchMapping("/")
    public InventoryResponseDTO updateInventory(@RequestBody InventoryUpdateRequestDTO requestDTO) throws InventoryNotFoundException {
        return inventoryService.updateInventory(requestDTO);
    }

    /***
     * Endpoint to delete an Inventory
     * @param id of an Inventory to be deleted
     * @throws InventoryNotFoundException is thrown if Inventory is not found
     */
    @DeleteMapping("/{id}")
    public void deleteInventoryById(@PathVariable Long id) throws InventoryNotFoundException {
        inventoryService.deleteInventoryById(id);
    }

    /***
     * Endpoint to check if products in the given order request specified by skuCodes are in stock and in required
     * quantity
     * @param requestDTO OrderInventoryDTO contains list of skuCodes and required quantity
     * @return OrderInventoryDTO contains list of skuCodes, required quantity, available quantity and flag
     * indicating if in stock or not
     */
    @GetMapping("/orderInStock/")
    public OrderInventoryDTO isInStock(@RequestBody OrderInventoryDTO requestDTO){
        return inventoryService.isInStock(requestDTO);
    }
}
