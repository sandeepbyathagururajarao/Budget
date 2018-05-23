package com.keyfalcon.budget.service.mapper;

import com.keyfalcon.budget.domain.*;
import com.keyfalcon.budget.service.dto.TCPDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TCP and its DTO TCPDTO.
 */
@Mapper(componentModel = "spring", uses = {UserDataMapper.class})
public interface TCPMapper extends EntityMapper<TCPDTO, TCP> {

    @Mapping(source = "user.id", target = "userId")
    TCPDTO toDto(TCP tCP);

    @Mapping(source = "userId", target = "user")
    TCP toEntity(TCPDTO tCPDTO);

    default TCP fromId(Long id) {
        if (id == null) {
            return null;
        }
        TCP tCP = new TCP();
        tCP.setId(id);
        return tCP;
    }
}
