package com.keyfalcon.budget.service;

import com.keyfalcon.budget.service.dto.ItemDTO;
import java.util.List;

/**
 * Service Interface for managing Item.
 */
public interface ItemService {

    /**
     * Save a item.
     *
     * @param itemDTO the entity to save
     * @return the persisted entity
     */
    ItemDTO save(ItemDTO itemDTO);

    /**
     * Get all the items.
     *
     * @return the list of entities
     */
    List<ItemDTO> findAll();

    /**
     * Get the "id" item.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ItemDTO findOne(Long id);

    /**
     * Delete the "id" item.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<ItemDTO> findAllFilteredItems(Long id);

    List<ItemDTO> searchItems(String itemName, Long id);

    List<ItemDTO> searchAllItems(String itemName);
}
