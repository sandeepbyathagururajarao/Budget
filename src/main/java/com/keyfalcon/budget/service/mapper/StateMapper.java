package com.keyfalcon.budget.service.mapper;

import com.keyfalcon.budget.domain.*;
import com.keyfalcon.budget.service.dto.StateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity State and its DTO StateDTO.
 */
@Mapper(componentModel = "spring", uses = {UserDataMapper.class})
public interface StateMapper extends EntityMapper<StateDTO, State> {

    @Mapping(source = "user.id", target = "userId")
    StateDTO toDto(State state);

    @Mapping(source = "userId", target = "user")
    State toEntity(StateDTO stateDTO);

    default State fromId(Long id) {
        if (id == null) {
            return null;
        }
        State state = new State();
        state.setId(id);
        return state;
    }
}
