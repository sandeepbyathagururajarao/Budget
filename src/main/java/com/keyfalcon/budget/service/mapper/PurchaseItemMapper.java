package com.keyfalcon.budget.service.mapper;

import com.keyfalcon.budget.domain.*;
import com.keyfalcon.budget.service.dto.PurchaseItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PurchaseItem and its DTO PurchaseItemDTO.
 */
@Mapper(componentModel = "spring", uses = {ItemMapper.class, GuidelineMapper.class, SubTypeMapper.class, TCPMapper.class, AreaMapper.class, UserDataMapper.class})
public interface PurchaseItemMapper extends EntityMapper<PurchaseItemDTO, PurchaseItem> {

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "guideline.id", target = "guidelineId")
    @Mapping(source = "subType.id", target = "subTypeId")
    @Mapping(source = "tcp.id", target = "tcpId")
    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "user.id", target = "userId")
    PurchaseItemDTO toDto(PurchaseItem purchaseItem);

    @Mapping(target = "subItems", ignore = true)
    @Mapping(source = "itemId", target = "item")
    @Mapping(source = "guidelineId", target = "guideline")
    @Mapping(source = "subTypeId", target = "subType")
    @Mapping(source = "tcpId", target = "tcp")
    @Mapping(source = "areaId", target = "area")
    @Mapping(source = "userId", target = "user")
    PurchaseItem toEntity(PurchaseItemDTO purchaseItemDTO);

    default PurchaseItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        PurchaseItem purchaseItem = new PurchaseItem();
        purchaseItem.setId(id);
        return purchaseItem;
    }
}
