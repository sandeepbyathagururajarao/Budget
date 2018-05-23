package com.keyfalcon.budget.service;

import com.keyfalcon.budget.service.dto.SubTypeDTO;
import java.util.List;

/**
 * Service Interface for managing SubType.
 */
public interface SubTypeService {

    /**
     * Save a subType.
     *
     * @param subTypeDTO the entity to save
     * @return the persisted entity
     */
    SubTypeDTO save(SubTypeDTO subTypeDTO);

    /**
     * Get all the subTypes.
     *
     * @return the list of entities
     */
    List<SubTypeDTO> findAll();

    /**
     * Get the "id" subType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SubTypeDTO findOne(Long id);

    /**
     * Delete the "id" subType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
