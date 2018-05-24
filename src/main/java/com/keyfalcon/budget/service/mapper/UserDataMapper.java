package com.keyfalcon.budget.service.mapper;

import com.keyfalcon.budget.domain.*;
import com.keyfalcon.budget.service.dto.UserDataDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserData and its DTO UserDataDTO.
 */
@Mapper(componentModel = "spring", uses = {StateMapper.class})
public interface UserDataMapper extends EntityMapper<UserDataDTO, UserData> {

    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "state.name", target = "stateName")
    UserDataDTO toDto(UserData userData);

    @Mapping(source = "stateId", target = "state")
    UserData toEntity(UserDataDTO userDataDTO);

    default UserData fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserData userData = new UserData();
        userData.setId(id);
        return userData;
    }
}
