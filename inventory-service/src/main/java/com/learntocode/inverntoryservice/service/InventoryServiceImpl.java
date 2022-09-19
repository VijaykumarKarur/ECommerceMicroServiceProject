package com.learntocode.inverntoryservice.service;

import com.learntocode.inverntoryservice.dto.InventoryRequestDTO;
import com.learntocode.inverntoryservice.dto.InventoryResponseDTO;
import com.learntocode.inverntoryservice.dto.InventoryUpdateRequestDTO;
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
        InventoryResponseDTO responseDTO =
                mapToInventoryResponseDTO(inventoryRepository.save(mapToInventory(requestDTO)));
        return responseDTO;
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
        if(!inventoryOptional.isPresent()){
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
        if(!inventoryOptional.isPresent()){
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
        if(!inventoryOptional.isPresent()){
            throw new InventoryNotFoundException("Inventory " + requestDTO.getId() + " Not Found");
        }
        Inventory inventory = inventoryOptional.get();
        if(Objects.nonNull(requestDTO.getSkuCode()) &&
        !"".equalsIgnoreCase(requestDTO.getSkuCode())){
            inventory.setSkuCode(requestDTO.getSkuCode());
        }
        if(Objects.nonNull(requestDTO.getQuantity()) &&
                !"".equalsIgnoreCase(requestDTO.getQuantity().toString())){
            inventory.setSkuCode(requestDTO.getQuantity().toString());
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
        if(!inventoryOptional.isPresent()){
            throw new InventoryNotFoundException("Inventory " + id + " Not Found");
        }
        inventoryRepository.delete(inventoryOptional.get());
    }

    public InventoryResponseDTO mapToInventoryResponseDTO(Inventory inventory){
        return InventoryResponseDTO
                .builder()
                .id(inventory.getId())
                .skuCode(inventory.getSkuCode())
                .quantity(inventory.getQuantity())
                .build();
    }

    public Inventory mapToInventory(InventoryRequestDTO requestDTO){
        Inventory inventory = new Inventory();
        inventory.setSkuCode(requestDTO.getSkuCode());
        inventory.setQuantity(requestDTO.getQuantity());
        return inventory;
    }
}
