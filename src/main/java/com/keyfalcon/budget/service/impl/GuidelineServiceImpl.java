package com.keyfalcon.budget.service.impl;

import com.keyfalcon.budget.service.GuidelineService;
import com.keyfalcon.budget.domain.Guideline;
import com.keyfalcon.budget.repository.GuidelineRepository;
import com.keyfalcon.budget.service.dto.GuidelineDTO;
import com.keyfalcon.budget.service.mapper.GuidelineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Guideline.
 */
@Service
@Transactional
public class GuidelineServiceImpl implements GuidelineService {

    private final Logger log = LoggerFactory.getLogger(GuidelineServiceImpl.class);

    private final GuidelineRepository guidelineRepository;

    private final GuidelineMapper guidelineMapper;

    public GuidelineServiceImpl(GuidelineRepository guidelineRepository, GuidelineMapper guidelineMapper) {
        this.guidelineRepository = guidelineRepository;
        this.guidelineMapper = guidelineMapper;
    }

    /**
     * Save a guideline.
     *
     * @param guidelineDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GuidelineDTO save(GuidelineDTO guidelineDTO) {
        log.debug("Request to save Guideline : {}", guidelineDTO);
        Guideline guideline = guidelineMapper.toEntity(guidelineDTO);
        guideline = guidelineRepository.save(guideline);
        return guidelineMapper.toDto(guideline);
    }

    /**
     * Get all the guidelines.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GuidelineDTO> findAll() {
        log.debug("Request to get all Guidelines");
        return guidelineRepository.findAll().stream()
            .map(guidelineMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one guideline by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public GuidelineDTO findOne(Long id) {
        log.debug("Request to get Guideline : {}", id);
        Guideline guideline = guidelineRepository.findOne(id);
        return guidelineMapper.toDto(guideline);
    }

    /**
     * Delete the guideline by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Guideline : {}", id);
        guidelineRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuidelineDTO> findAllFilteredGuidelines(Long id) {
        log.debug("Request to get all filtered States");
        return guidelineRepository.findAllByUserId(id).stream()
            .map(guidelineMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
