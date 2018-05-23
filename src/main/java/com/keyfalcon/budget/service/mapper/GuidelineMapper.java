package com.keyfalcon.budget.service.mapper;

import com.keyfalcon.budget.domain.*;
import com.keyfalcon.budget.service.dto.GuidelineDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Guideline and its DTO GuidelineDTO.
 */
@Mapper(componentModel = "spring", uses = {UserDataMapper.class})
public interface GuidelineMapper extends EntityMapper<GuidelineDTO, Guideline> {

    @Mapping(source = "user.id", target = "userId")
    GuidelineDTO toDto(Guideline guideline);

    @Mapping(source = "userId", target = "user")
    Guideline toEntity(GuidelineDTO guidelineDTO);

    default Guideline fromId(Long id) {
        if (id == null) {
            return null;
        }
        Guideline guideline = new Guideline();
        guideline.setId(id);
        return guideline;
    }
}
