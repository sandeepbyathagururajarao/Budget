package com.keyfalcon.budget.service;

import com.keyfalcon.budget.service.dto.StateDTO;
import java.util.List;

/**
 * Service Interface for managing State.
 */
public interface StateService {

    /**
     * Save a state.
     *
     * @param stateDTO the entity to save
     * @return the persisted entity
     */
    StateDTO save(StateDTO stateDTO);

    /**
     * Get all the states.
     *
     * @return the list of entities
     */
    List<StateDTO> findAll();

    /**
     * Get the "id" state.
     *
     * @param id the id of the entity
     * @return the entity
     */
    StateDTO findOne(Long id);

    /**
     * Delete the "id" state.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
