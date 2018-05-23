package com.keyfalcon.budget.service.mapper;

import com.keyfalcon.budget.domain.*;
import com.keyfalcon.budget.service.dto.AreaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Area and its DTO AreaDTO.
 */
@Mapper(componentModel = "spring", uses = {UserDataMapper.class})
public interface AreaMapper extends EntityMapper<AreaDTO, Area> {

    @Mapping(source = "user.id", target = "userId")
    AreaDTO toDto(Area area);

    @Mapping(source = "userId", target = "user")
    Area toEntity(AreaDTO areaDTO);

    default Area fromId(Long id) {
        if (id == null) {
            return null;
        }
        Area area = new Area();
        area.setId(id);
        return area;
    }
}
