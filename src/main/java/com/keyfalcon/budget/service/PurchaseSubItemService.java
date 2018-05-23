package com.keyfalcon.budget.service;

import com.keyfalcon.budget.service.dto.PurchaseSubItemDTO;
import java.util.List;

/**
 * Service Interface for managing PurchaseSubItem.
 */
public interface PurchaseSubItemService {

    /**
     * Save a purchaseSubItem.
     *
     * @param purchaseSubItemDTO the entity to save
     * @return the persisted entity
     */
    PurchaseSubItemDTO save(PurchaseSubItemDTO purchaseSubItemDTO);

    /**
     * Get all the purchaseSubItems.
     *
     * @return the list of entities
     */
    List<PurchaseSubItemDTO> findAll();

    /**
     * Get the "id" purchaseSubItem.
     *
     * @param id the id of the entity
     * @return the entity
     */
    PurchaseSubItemDTO findOne(Long id);

    /**
     * Delete the "id" purchaseSubItem.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
