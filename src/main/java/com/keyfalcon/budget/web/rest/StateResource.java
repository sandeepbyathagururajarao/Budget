package com.keyfalcon.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.keyfalcon.budget.Role;
import com.keyfalcon.budget.service.StateService;
import com.keyfalcon.budget.web.rest.errors.BadRequestAlertException;
import com.keyfalcon.budget.web.rest.util.HeaderUtil;
import com.keyfalcon.budget.service.dto.StateDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing State.
 */
@RestController
@RequestMapping("/api")
public class StateResource {

    private final Logger log = LoggerFactory.getLogger(StateResource.class);

    private static final String ENTITY_NAME = "state";

    private final StateService stateService;

    public StateResource(StateService stateService) {
        this.stateService = stateService;
    }

    /**
     * POST  /states : Create a new state.
     *
     * @param stateDTO the stateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stateDTO, or with status 400 (Bad Request) if the state has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/states")
    @Timed
    public ResponseEntity<StateDTO> createState(@Valid @RequestBody StateDTO stateDTO) throws URISyntaxException {
        log.debug("REST request to save State : {}", stateDTO);
        if (stateDTO.getId() != null) {
            throw new BadRequestAlertException("A new state cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StateDTO result = stateService.save(stateDTO);
        return ResponseEntity.created(new URI("/api/states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /states : Updates an existing state.
     *
     * @param stateDTO the stateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stateDTO,
     * or with status 400 (Bad Request) if the stateDTO is not valid,
     * or with status 500 (Internal Server Error) if the stateDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/states")
    @Timed
    public ResponseEntity<StateDTO> updateState(@Valid @RequestBody StateDTO stateDTO) throws URISyntaxException {
        log.debug("REST request to update State : {}", stateDTO);
        if (stateDTO.getId() == null) {
            return createState(stateDTO);
        }
        StateDTO result = stateService.save(stateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /states : get all the states.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of states in body
     */
    @GetMapping("/states")
    @Timed
    public List<StateDTO> getAllStates() {
        log.debug("REST request to get all States");
        return stateService.findAll();
        }

    /**
     * GET  /states/:id : get the "id" state.
     *
     * @param id the id of the stateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/states/{id}")
    @Timed
    public ResponseEntity<StateDTO> getState(@PathVariable Long id) {
        log.debug("REST request to get State : {}", id);
        StateDTO stateDTO = stateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stateDTO));
    }

    /**
     * DELETE  /states/:id : delete the "id" state.
     *
     * @param id the id of the stateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/states/{id}")
    @Timed
    public ResponseEntity<Void> deleteState(@PathVariable Long id) {
        log.debug("REST request to delete State : {}", id);
        stateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /states/filter/{userRole}/{id} : get all filtered the states.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of states in body
     */
    @GetMapping("/states/filter/{userRole}/{id}")
    @Timed
    public List<StateDTO> getFilteredStates(@PathVariable Long userRole, @PathVariable Long id) {
        log.debug("REST request to get all filtered States");
        List<StateDTO> stateDTOList = null;
        if(Role.getValue(userRole) == Role.SUPERADMIN) {
            stateDTOList = stateService.findAll();
        } else {
            stateDTOList = stateService.findAllFilteredStates(id);
        }
        return stateDTOList;
    }

    @GetMapping("/states/search/{name}/{userRole}/{id}")
    @Timed
    public List<StateDTO> getSearchStates(@PathVariable String name, @PathVariable Long userRole, @PathVariable Long id) {
        log.debug("REST request to get all searched guidelines");
        if(name != null) {
            if("@~all~@".equals(name.trim())) {
                return getFilteredStates(userRole, id);
            }
        }
        List<StateDTO> stateDTOList = null;
        if(Role.getValue(userRole) == Role.SUPERADMIN) {
            stateDTOList = stateService.searchAllItems(name);
        } else {
            stateDTOList = stateService.searchItems(name, id);
        }
        return stateDTOList;
    }
}
