package com.keyfalcon.budget.service.mapper;

import com.keyfalcon.budget.domain.*;
import com.keyfalcon.budget.service.dto.PurchaseSubItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PurchaseSubItem and its DTO PurchaseSubItemDTO.
 */
@Mapper(componentModel = "spring", uses = {PurchaseItemMapper.class})
public interface PurchaseSubItemMapper extends EntityMapper<PurchaseSubItemDTO, PurchaseSubItem> {

    @Mapping(source = "purchaseItem.id", target = "purchaseItemId")
    PurchaseSubItemDTO toDto(PurchaseSubItem purchaseSubItem);

    @Mapping(source = "purchaseItemId", target = "purchaseItem")
    PurchaseSubItem toEntity(PurchaseSubItemDTO purchaseSubItemDTO);

    default PurchaseSubItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        PurchaseSubItem purchaseSubItem = new PurchaseSubItem();
        purchaseSubItem.setId(id);
        return purchaseSubItem;
    }
}
