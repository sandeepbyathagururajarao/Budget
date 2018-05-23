package com.keyfalcon.budget.service;

import com.keyfalcon.budget.service.dto.GuidelineDTO;
import java.util.List;

/**
 * Service Interface for managing Guideline.
 */
public interface GuidelineService {

    /**
     * Save a guideline.
     *
     * @param guidelineDTO the entity to save
     * @return the persisted entity
     */
    GuidelineDTO save(GuidelineDTO guidelineDTO);

    /**
     * Get all the guidelines.
     *
     * @return the list of entities
     */
    List<GuidelineDTO> findAll();

    /**
     * Get the "id" guideline.
     *
     * @param id the id of the entity
     * @return the entity
     */
    GuidelineDTO findOne(Long id);

    /**
     * Delete the "id" guideline.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
