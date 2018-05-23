package com.keyfalcon.budget.service.impl;

import com.keyfalcon.budget.service.AreaService;
import com.keyfalcon.budget.domain.Area;
import com.keyfalcon.budget.repository.AreaRepository;
import com.keyfalcon.budget.service.dto.AreaDTO;
import com.keyfalcon.budget.service.mapper.AreaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Area.
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService {

    private final Logger log = LoggerFactory.getLogger(AreaServiceImpl.class);

    private final AreaRepository areaRepository;

    private final AreaMapper areaMapper;

    public AreaServiceImpl(AreaRepository areaRepository, AreaMapper areaMapper) {
        this.areaRepository = areaRepository;
        this.areaMapper = areaMapper;
    }

    /**
     * Save a area.
     *
     * @param areaDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AreaDTO save(AreaDTO areaDTO) {
        log.debug("Request to save Area : {}", areaDTO);
        Area area = areaMapper.toEntity(areaDTO);
        area = areaRepository.save(area);
        return areaMapper.toDto(area);
    }

    /**
     * Get all the areas.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AreaDTO> findAll() {
        log.debug("Request to get all Areas");
        return areaRepository.findAll().stream()
            .map(areaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one area by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AreaDTO findOne(Long id) {
        log.debug("Request to get Area : {}", id);
        Area area = areaRepository.findOne(id);
        return areaMapper.toDto(area);
    }

    /**
     * Delete the area by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Area : {}", id);
        areaRepository.delete(id);
    }
}
