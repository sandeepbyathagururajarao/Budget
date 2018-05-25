package com.keyfalcon.budget.service;

import com.keyfalcon.budget.service.dto.TCPDTO;
import java.util.List;

/**
 * Service Interface for managing TCP.
 */
public interface TCPService {

    /**
     * Save a tCP.
     *
     * @param tCPDTO the entity to save
     * @return the persisted entity
     */
    TCPDTO save(TCPDTO tCPDTO);

    /**
     * Get all the tCPS.
     *
     * @return the list of entities
     */
    List<TCPDTO> findAll();

    /**
     * Get the "id" tCP.
     *
     * @param id the id of the entity
     * @return the entity
     */
    TCPDTO findOne(Long id);

    /**
     * Delete the "id" tCP.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<TCPDTO> findAllFilteredItems(Long id);
}
