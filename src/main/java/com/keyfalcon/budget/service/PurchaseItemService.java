package com.keyfalcon.budget.service;

import com.keyfalcon.budget.service.dto.PurchaseItemDTO;
import java.util.List;

/**
 * Service Interface for managing PurchaseItem.
 */
public interface PurchaseItemService {

    /**
     * Save a purchaseItem.
     *
     * @param purchaseItemDTO the entity to save
     * @return the persisted entity
     */
    PurchaseItemDTO save(PurchaseItemDTO purchaseItemDTO);

    /**
     * Get all the purchaseItems.
     *
     * @return the list of entities
     */
    List<PurchaseItemDTO> findAll();

    /**
     * Get the "id" purchaseItem.
     *
     * @param id the id of the entity
     * @return the entity
     */
    PurchaseItemDTO findOne(Long id);

    /**
     * Delete the "id" purchaseItem.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<PurchaseItemDTO> findAllFilteredRecurringItems(Long id);

    List<PurchaseItemDTO> findAllFilteredNonRecurringItems(Long id);

    List<PurchaseItemDTO> findAllFilteredByPurchaseType(String purchaseType);
}
