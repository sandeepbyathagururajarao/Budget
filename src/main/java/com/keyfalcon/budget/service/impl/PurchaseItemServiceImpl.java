package com.keyfalcon.budget.service.impl;

import com.keyfalcon.budget.service.PurchaseItemService;
import com.keyfalcon.budget.domain.PurchaseItem;
import com.keyfalcon.budget.repository.PurchaseItemRepository;
import com.keyfalcon.budget.service.dto.PurchaseItemDTO;
import com.keyfalcon.budget.service.mapper.PurchaseItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PurchaseItem.
 */
@Service
@Transactional
public class PurchaseItemServiceImpl implements PurchaseItemService {

    private final Logger log = LoggerFactory.getLogger(PurchaseItemServiceImpl.class);

    private final PurchaseItemRepository purchaseItemRepository;

    private final PurchaseItemMapper purchaseItemMapper;

    public PurchaseItemServiceImpl(PurchaseItemRepository purchaseItemRepository, PurchaseItemMapper purchaseItemMapper) {
        this.purchaseItemRepository = purchaseItemRepository;
        this.purchaseItemMapper = purchaseItemMapper;
    }

    /**
     * Save a purchaseItem.
     *
     * @param purchaseItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PurchaseItemDTO save(PurchaseItemDTO purchaseItemDTO) {
        log.debug("Request to save PurchaseItem : {}", purchaseItemDTO);
        PurchaseItem purchaseItem = purchaseItemMapper.toEntity(purchaseItemDTO);
        purchaseItem = purchaseItemRepository.save(purchaseItem);
        return purchaseItemMapper.toDto(purchaseItem);
    }

    /**
     * Get all the purchaseItems.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseItemDTO> findAll() {
        log.debug("Request to get all PurchaseItems");
        return purchaseItemRepository.findAll().stream()
            .map(purchaseItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one purchaseItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PurchaseItemDTO findOne(Long id) {
        log.debug("Request to get PurchaseItem : {}", id);
        PurchaseItem purchaseItem = purchaseItemRepository.findOne(id);
        return purchaseItemMapper.toDto(purchaseItem);
    }

    /**
     * Delete the purchaseItem by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PurchaseItem : {}", id);
        purchaseItemRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseItemDTO> findAllFilteredRecurringItems(Long userId) {
        log.debug("Request to get all filtered States");
        return purchaseItemRepository.findAllByUserIdAndPurchaseType(userId,"1").stream()
            .map(purchaseItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseItemDTO> findAllFilteredNonRecurringItems(Long userId) {
        log.debug("Request to get all filtered States");
        return purchaseItemRepository.findAllByUserIdAndPurchaseType(userId,"2").stream()
            .map(purchaseItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseItemDTO> findAllFilteredByPurchaseType(String purchaseType) {
        log.debug("Request to get all filtered States");
        return purchaseItemRepository.findAllByPurchaseType(purchaseType).stream()
            .map(purchaseItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<PurchaseItemDTO> findAllFilteredByApprovalAndPurchaseType(String approvalStatus, String purchaseType) {
        log.debug("Request to get all filtered States");
        return purchaseItemRepository.findAllByApprovalStatusAndPurchaseType(approvalStatus, purchaseType).stream()
            .map(purchaseItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<PurchaseItemDTO> findAllFilteredApprovalAndRecurringItems(String approvalStatus, Long userId) {
        log.debug("Request to get all filtered States");
        return purchaseItemRepository.findAllByApprovalStatusAndUserIdAndPurchaseType(approvalStatus, userId,"1").stream()
            .map(purchaseItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<PurchaseItemDTO> findAllFilteredApprovalAndNonRecurringItems(String approvalStatus, Long userId) {
        log.debug("Request to get all filtered States");
        return purchaseItemRepository.findAllByApprovalStatusAndUserIdAndPurchaseType(approvalStatus, userId,"2").stream()
            .map(purchaseItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Long countAll() {
        return purchaseItemRepository.countAllBy();
    }

    @Override
    public Long countByUserId(Long userId) {
        return purchaseItemRepository.countByUserId(userId);
    }

    @Override
    public Long countApprovalByUserId(Long userId) {
        return purchaseItemRepository.countAllByApprovalStatusAndUserId("1", userId);
    }

    @Override
    public Long countAllByApprovalStatus() {
        return purchaseItemRepository.countAllByApprovalStatus("1");
    }


}
