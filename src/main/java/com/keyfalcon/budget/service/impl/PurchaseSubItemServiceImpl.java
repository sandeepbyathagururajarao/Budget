package com.keyfalcon.budget.service.impl;

import com.keyfalcon.budget.service.PurchaseSubItemService;
import com.keyfalcon.budget.domain.PurchaseSubItem;
import com.keyfalcon.budget.repository.PurchaseSubItemRepository;
import com.keyfalcon.budget.service.dto.PurchaseSubItemDTO;
import com.keyfalcon.budget.service.mapper.PurchaseSubItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PurchaseSubItem.
 */
@Service
@Transactional
public class PurchaseSubItemServiceImpl implements PurchaseSubItemService {

    private final Logger log = LoggerFactory.getLogger(PurchaseSubItemServiceImpl.class);

    private final PurchaseSubItemRepository purchaseSubItemRepository;

    private final PurchaseSubItemMapper purchaseSubItemMapper;

    public PurchaseSubItemServiceImpl(PurchaseSubItemRepository purchaseSubItemRepository, PurchaseSubItemMapper purchaseSubItemMapper) {
        this.purchaseSubItemRepository = purchaseSubItemRepository;
        this.purchaseSubItemMapper = purchaseSubItemMapper;
    }

    /**
     * Save a purchaseSubItem.
     *
     * @param purchaseSubItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PurchaseSubItemDTO save(PurchaseSubItemDTO purchaseSubItemDTO) {
        log.debug("Request to save PurchaseSubItem : {}", purchaseSubItemDTO);
        PurchaseSubItem purchaseSubItem = purchaseSubItemMapper.toEntity(purchaseSubItemDTO);
        purchaseSubItem = purchaseSubItemRepository.save(purchaseSubItem);
        return purchaseSubItemMapper.toDto(purchaseSubItem);
    }

    /**
     * Get all the purchaseSubItems.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseSubItemDTO> findAll() {
        log.debug("Request to get all PurchaseSubItems");
        return purchaseSubItemRepository.findAll().stream()
            .map(purchaseSubItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one purchaseSubItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PurchaseSubItemDTO findOne(Long id) {
        log.debug("Request to get PurchaseSubItem : {}", id);
        PurchaseSubItem purchaseSubItem = purchaseSubItemRepository.findOne(id);
        return purchaseSubItemMapper.toDto(purchaseSubItem);
    }

    /**
     * Delete the purchaseSubItem by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PurchaseSubItem : {}", id);
        purchaseSubItemRepository.delete(id);
    }

    public boolean saveAll(Set<PurchaseSubItemDTO> purchaseSubItemDTOList) {
        List<PurchaseSubItem> purchaseSubItemList = new ArrayList<>();
        for(PurchaseSubItemDTO purchaseSubItemDTO:purchaseSubItemDTOList) {
            purchaseSubItemList.add(purchaseSubItemMapper.toEntity(purchaseSubItemDTO));
        }
        purchaseSubItemRepository.save(purchaseSubItemList);
        return true;
    }

    public int deleteAllByPurchaseId(Long id) {
        return purchaseSubItemRepository.deleteAllByPurchaseItemId(id);
    }
}
