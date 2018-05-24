package com.keyfalcon.budget.service.mapper;

import com.keyfalcon.budget.domain.*;
import com.keyfalcon.budget.service.dto.SubTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SubType and its DTO SubTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {UserDataMapper.class})
public interface SubTypeMapper extends EntityMapper<SubTypeDTO, SubType> {

    @Mapping(source = "user.id", target = "userId")
    SubTypeDTO toDto(SubType subType);

    @Mapping(source = "userId", target = "user")
    SubType toEntity(SubTypeDTO subTypeDTO);

    default SubType fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubType subType = new SubType();
        subType.setId(id);
        return subType;
    }
}
