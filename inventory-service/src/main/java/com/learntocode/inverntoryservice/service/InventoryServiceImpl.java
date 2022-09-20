package com.learntocode.inverntoryservice.service;

import com.learntocode.inverntoryservice.dto.*;
import com.learntocode.inverntoryservice.exception.InventoryNotFoundException;
import com.learntocode.inverntoryservice.model.Inventory;
import com.learntocode.inverntoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService{
    @Autowired
    private InventoryRepository inventoryRepository;

    /***
     * Method maps requestDTO to inventory object, invokes repository save method to save Inventory
     * and return Inventory response object mapped to responseDTO object
     * @param requestDTO request conatins skuCode and quantity
     * @return InventoryResponseDTO contains id, skuCode and quantity
     */
    @Override
    public InventoryResponseDTO createInventory(InventoryRequestDTO requestDTO) {
        return mapToInventoryResponseDTO(inventoryRepository.save(mapToInventory(requestDTO)));
    }

    /***
     * Method returns entire list of Inventories. Inventory objects are mapped to InventoryResponseDTO objects
     * @return List of InventoryResponseDTO objects
     */
    @Override
    public List<InventoryResponseDTO> getAllInventories() {
        List<Inventory> inventoryList = inventoryRepository.findAll();
        return inventoryList.stream().map(this::mapToInventoryResponseDTO).toList();
    }

    /***
     * Method to retrieve Inventory based on id
     * @param id Inventory id
     * @return InventoryResponseDTO containing id, skuCode and quantity
     * @throws InventoryNotFoundException exception is thrown if inventory is not present
     */
    @Override
    public InventoryResponseDTO getInventoryById(Long id) throws InventoryNotFoundException {
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);
        if(inventoryOptional.isEmpty()){
            throw new InventoryNotFoundException("Inventory " + id + " Not Found");
        }
        return mapToInventoryResponseDTO(inventoryOptional.get());
    }

    /***
     * Method retrieves Inventory object based on skuCode
     * @param skuCode of Inventory
     * @return InventoryResponseDTO corresponding to Inventory of particular skuCode
     * @throws InventoryNotFoundException exception is throws if inventory is not present
     */
    @Override
    public InventoryResponseDTO getInventoryBySkuCodeIgnoreCase(String skuCode) throws InventoryNotFoundException {
        Optional<Inventory> inventoryOptional = inventoryRepository.findBySkuCodeIgnoreCase(skuCode);
        if(inventoryOptional.isEmpty()){
            throw new InventoryNotFoundException("Inventory " + skuCode + " Not Found");
        }
        return mapToInventoryResponseDTO(inventoryOptional.get());
    }

    /***
     * Method to udpate Inventory details
     * @param requestDTO contains necessary update information for a particular inventory
     * @return InventoryResponseDTO contains the updated information
     * @throws InventoryNotFoundException exception is thrown if inventory is not found
     */
    @Override
    public InventoryResponseDTO updateInventory(InventoryUpdateRequestDTO requestDTO) throws InventoryNotFoundException {
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(requestDTO.getId());
        if(inventoryOptional.isEmpty()){
            throw new InventoryNotFoundException("Inventory " + requestDTO.getId() + " Not Found");
        }
        Inventory inventory = inventoryOptional.get();
        if(Objects.nonNull(requestDTO.getSkuCode()) &&
        !"".equalsIgnoreCase(requestDTO.getSkuCode())){
            inventory.setSkuCode(requestDTO.getSkuCode());
        }
        if(Objects.nonNull(requestDTO.getQuantity()) &&
                !"".equalsIgnoreCase(requestDTO.getQuantity().toString())){
            inventory.setQuantity(requestDTO.getQuantity());
        }
        return mapToInventoryResponseDTO(inventoryRepository.save(inventory));
    }

    /***
     * Method to delete Inventory based on id
     * @param id of the inventory
     * @throws InventoryNotFoundException exception is thrown if inventory is not found
     */

    @Override
    public void deleteInventoryById(Long id) throws InventoryNotFoundException {
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);
        if(inventoryOptional.isEmpty()){
            throw new InventoryNotFoundException("Inventory " + id + " Not Found");
        }
        inventoryRepository.delete(inventoryOptional.get());
    }

    /***
     * Method to check if requested Products are in Stock and in required quantity
     * @param requestDTO OrderInventoryDTO contains list of skuCodes corresponding to products, and the required
     *                   quantity
     * @return OrderInventoryDTO contains list of skuCodes, required quantity, available quantity and finally
     * status indicating if in stock or not
     */
    @Override
    public OrderInventoryDTO isInStock(OrderInventoryDTO requestDTO) {
        OrderInventoryDTO responseDTO = new OrderInventoryDTO();
        List<OrderLineItemInventoryDTO> orderLineItemInventoryDTOList = requestDTO
                .getOrderLineItemInventoryDTOList()
                .stream()
                .map(this::checkInventoryStock)
                .toList();
        responseDTO.setOrderLineItemInventoryDTOList(orderLineItemInventoryDTOList);
        return responseDTO;
    }

    /***
     * Helper method to check if each Product is present in stock and in required quantity
     * @param requestDTO OrderLineIemInventoryDTO contains the skuCode and required quantity
     * @return OrderLineItemInventoryDTO contains skuCode, required quantity, available quantity and in stock
     * flag
     */
    public OrderLineItemInventoryDTO checkInventoryStock(OrderLineItemInventoryDTO requestDTO){
        OrderLineItemInventoryDTO responseDTO = new OrderLineItemInventoryDTO();
        responseDTO.setSkuCode(requestDTO.getSkuCode());
        responseDTO.setRequiredQuantity(requestDTO.getRequiredQuantity());
        Optional<Inventory> inventoryOptional =
                inventoryRepository.findBySkuCodeIgnoreCase(requestDTO.getSkuCode());

        /* If
            product not in inventory, indicate quantity as 0 and in stock as false
           Else
            indicate the actual quantity and in stock if available qty >= required qty
         */
        if(inventoryOptional.isEmpty()){
            responseDTO.setAvailableQuantity(0);
            responseDTO.setInStock(false);
        }
        else{
            responseDTO.setAvailableQuantity(inventoryOptional.get().getQuantity());
            responseDTO.setInStock(inventoryOptional.get().getQuantity() >= requestDTO.getRequiredQuantity());
        }
        return responseDTO;
    }

    /***
     * Helper method to map Inventory model to InventoryResponseDTO
     * @param inventory Inventory object to be mapped
     * @return InventoryResponseDTO
     */
    public InventoryResponseDTO mapToInventoryResponseDTO(Inventory inventory){
        return InventoryResponseDTO
                .builder()
                .id(inventory.getId())
                .skuCode(inventory.getSkuCode())
                .quantity(inventory.getQuantity())
                .build();
    }

    /***
     * Helper method to map InventoryRequestDTO to Inventory model
     * @param requestDTO InventoryRequestDTO to be mapped
     * @return Inventory
     */
    public Inventory mapToInventory(InventoryRequestDTO requestDTO){
        Inventory inventory = new Inventory();
        inventory.setSkuCode(requestDTO.getSkuCode());
        inventory.setQuantity(requestDTO.getQuantity());
        return inventory;
    }
}
