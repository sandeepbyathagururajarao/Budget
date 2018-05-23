package com.keyfalcon.budget.service;

import com.keyfalcon.budget.service.dto.AreaDTO;
import java.util.List;

/**
 * Service Interface for managing Area.
 */
public interface AreaService {

    /**
     * Save a area.
     *
     * @param areaDTO the entity to save
     * @return the persisted entity
     */
    AreaDTO save(AreaDTO areaDTO);

    /**
     * Get all the areas.
     *
     * @return the list of entities
     */
    List<AreaDTO> findAll();

    /**
     * Get the "id" area.
     *
     * @param id the id of the entity
     * @return the entity
     */
    AreaDTO findOne(Long id);

    /**
     * Delete the "id" area.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
