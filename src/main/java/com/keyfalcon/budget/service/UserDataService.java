package com.keyfalcon.budget.service;

import com.keyfalcon.budget.service.dto.UserDataDTO;
import java.util.List;

/**
 * Service Interface for managing UserData.
 */
public interface UserDataService {

    /**
     * Save a userData.
     *
     * @param userDataDTO the entity to save
     * @return the persisted entity
     */
    UserDataDTO save(UserDataDTO userDataDTO);

    /**
     * Get all the userData.
     *
     * @return the list of entities
     */
    List<UserDataDTO> findAll();

    /**
     * Get the "id" userData.
     *
     * @param id the id of the entity
     * @return the entity
     */
    UserDataDTO findOne(Long id);

    /**
     * Delete the "id" userData.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Get the "userId" userData.
     *
     * @param userId the userId of the entity
     */
    List<UserDataDTO> getUserDataByUserId(String userId);


    /**
     * Get the created "userId" userData.
     *
     * @param criteria the userId of the entity
     */
    List<UserDataDTO> findAllByCriteria(String criteria);


    /**
     * Get the created "userId" userData.
     *
     * @param userId the userId of the entity
     */
    List<UserDataDTO> findAllByCreatedByAndCriteria(String userId, String criteria);

    Long countAll();

    Long countByCreatedByAndUserType(String userId, String criteria);

    Long countByCreatedBy(String userId);

}
