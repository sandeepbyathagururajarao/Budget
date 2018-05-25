package com.keyfalcon.budget.service.impl;

import com.keyfalcon.budget.service.UserDataService;
import com.keyfalcon.budget.domain.UserData;
import com.keyfalcon.budget.repository.UserDataRepository;
import com.keyfalcon.budget.service.dto.UserDataDTO;
import com.keyfalcon.budget.service.mapper.UserDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing UserData.
 */
@Service
@Transactional
public class UserDataServiceImpl implements UserDataService {

    private final Logger log = LoggerFactory.getLogger(UserDataServiceImpl.class);

    private final UserDataRepository userDataRepository;

    private final UserDataMapper userDataMapper;

    public UserDataServiceImpl(UserDataRepository userDataRepository, UserDataMapper userDataMapper) {
        this.userDataRepository = userDataRepository;
        this.userDataMapper = userDataMapper;
    }

    /**
     * Save a userData.
     *
     * @param userDataDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserDataDTO save(UserDataDTO userDataDTO) {
        log.debug("Request to save UserData : {}", userDataDTO);
        UserData userData = userDataMapper.toEntity(userDataDTO);
        userData = userDataRepository.save(userData);
        return userDataMapper.toDto(userData);
    }

    /**
     * Get all the userData.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDataDTO> findAll() {
        log.debug("Request to get all UserData");
        return userDataRepository.findAll().stream()
            .map(userDataMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one userData by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserDataDTO findOne(Long id) {
        log.debug("Request to get UserData : {}", id);
        UserData userData = userDataRepository.findOne(id);
        return userDataMapper.toDto(userData);
    }

    /**
     * Delete the userData by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserData : {}", id);
        userDataRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDataDTO> getUserDataByUserId(String userId) {
        return userDataRepository.findAllByUserId(userId).stream()
            .map(userDataMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<UserDataDTO> findAllByCriteria(String criteria) {
        return userDataRepository.findAllByUserType(criteria).stream()
            .map(userDataMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<UserDataDTO> findAllByCreatedByAndCriteria(String userId, String criteria) {
        return userDataRepository.findAllByCreatedByAndUserType(userId,criteria).stream()
            .map(userDataMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
