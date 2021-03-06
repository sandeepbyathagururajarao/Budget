package com.keyfalcon.budget.service.impl;

import com.keyfalcon.budget.service.ItemService;
import com.keyfalcon.budget.domain.Item;
import com.keyfalcon.budget.repository.ItemRepository;
import com.keyfalcon.budget.service.dto.ItemDTO;
import com.keyfalcon.budget.service.mapper.ItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Item.
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    /**
     * Save a item.
     *
     * @param itemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ItemDTO save(ItemDTO itemDTO) {
        log.debug("Request to save Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    /**
     * Get all the items.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> findAll() {
        log.debug("Request to get all Items");
        return itemRepository.findAll().stream()
            .map(itemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one item by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ItemDTO findOne(Long id) {
        log.debug("Request to get Item : {}", id);
        Item item = itemRepository.findOne(id);
        return itemMapper.toDto(item);
    }

    /**
     * Delete the item by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> findAllFilteredItems(Long id) {
        log.debug("Request to get all filtered States");
        return itemRepository.findAllByUserId(id).stream()
            .map(itemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> searchItems(String itemName, Long id) {
        log.debug("Request to get all filtered Items based on User");
        return itemRepository.findAllByItemNameContainsAndUserId(itemName, id).stream()
            .map(itemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> searchAllItems(String itemName) {
        log.debug("Request to get all filtered Items");
        return itemRepository.findAllByItemNameContains(itemName).stream()
            .map(itemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

}
