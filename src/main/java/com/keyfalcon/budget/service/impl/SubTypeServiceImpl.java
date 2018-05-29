package com.keyfalcon.budget.service.impl;

import com.keyfalcon.budget.service.SubTypeService;
import com.keyfalcon.budget.domain.SubType;
import com.keyfalcon.budget.repository.SubTypeRepository;
import com.keyfalcon.budget.service.dto.SubTypeDTO;
import com.keyfalcon.budget.service.mapper.SubTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SubType.
 */
@Service
@Transactional
public class SubTypeServiceImpl implements SubTypeService {

    private final Logger log = LoggerFactory.getLogger(SubTypeServiceImpl.class);

    private final SubTypeRepository subTypeRepository;

    private final SubTypeMapper subTypeMapper;

    public SubTypeServiceImpl(SubTypeRepository subTypeRepository, SubTypeMapper subTypeMapper) {
        this.subTypeRepository = subTypeRepository;
        this.subTypeMapper = subTypeMapper;
    }

    /**
     * Save a subType.
     *
     * @param subTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SubTypeDTO save(SubTypeDTO subTypeDTO) {
        log.debug("Request to save SubType : {}", subTypeDTO);
        SubType subType = subTypeMapper.toEntity(subTypeDTO);
        subType = subTypeRepository.save(subType);
        return subTypeMapper.toDto(subType);
    }

    /**
     * Get all the subTypes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SubTypeDTO> findAll() {
        log.debug("Request to get all SubTypes");
        return subTypeRepository.findAll().stream()
            .map(subTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one subType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SubTypeDTO findOne(Long id) {
        log.debug("Request to get SubType : {}", id);
        SubType subType = subTypeRepository.findOne(id);
        return subTypeMapper.toDto(subType);
    }

    /**
     * Delete the subType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubType : {}", id);
        subTypeRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubTypeDTO> findAllFilteredItems(Long id) {
        log.debug("Request to get all filtered States");
        return subTypeRepository.findAllByUserId(id).stream()
            .map(subTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubTypeDTO> searchItems(String subTypeNumber, Long id) {
        log.debug("Request to get all filtered SubTypeNumbers based on User");
        return subTypeRepository.findAllBySubTypeNumberContainsAndUserId(subTypeNumber, id).stream()
            .map(subTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubTypeDTO> searchAllItems(String subTypeNumber) {
        log.debug("Request to get all filtered SubTypeNumbers");
        return subTypeRepository.findAllBySubTypeNumberContains(subTypeNumber).stream()
            .map(subTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
