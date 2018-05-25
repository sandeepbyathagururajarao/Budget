package com.keyfalcon.budget.service.impl;

import com.keyfalcon.budget.service.TCPService;
import com.keyfalcon.budget.domain.TCP;
import com.keyfalcon.budget.repository.TCPRepository;
import com.keyfalcon.budget.service.dto.TCPDTO;
import com.keyfalcon.budget.service.mapper.TCPMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing TCP.
 */
@Service
@Transactional
public class TCPServiceImpl implements TCPService {

    private final Logger log = LoggerFactory.getLogger(TCPServiceImpl.class);

    private final TCPRepository tCPRepository;

    private final TCPMapper tCPMapper;

    public TCPServiceImpl(TCPRepository tCPRepository, TCPMapper tCPMapper) {
        this.tCPRepository = tCPRepository;
        this.tCPMapper = tCPMapper;
    }

    /**
     * Save a tCP.
     *
     * @param tCPDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TCPDTO save(TCPDTO tCPDTO) {
        log.debug("Request to save TCP : {}", tCPDTO);
        TCP tCP = tCPMapper.toEntity(tCPDTO);
        tCP = tCPRepository.save(tCP);
        return tCPMapper.toDto(tCP);
    }

    /**
     * Get all the tCPS.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TCPDTO> findAll() {
        log.debug("Request to get all TCPS");
        return tCPRepository.findAll().stream()
            .map(tCPMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one tCP by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TCPDTO findOne(Long id) {
        log.debug("Request to get TCP : {}", id);
        TCP tCP = tCPRepository.findOne(id);
        return tCPMapper.toDto(tCP);
    }

    /**
     * Delete the tCP by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TCP : {}", id);
        tCPRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TCPDTO> findAllFilteredItems(Long id) {
        log.debug("Request to get all filtered States");
        return tCPRepository.findAllByUserId(id).stream()
            .map(tCPMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
