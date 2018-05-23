package com.keyfalcon.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.keyfalcon.budget.service.SubTypeService;
import com.keyfalcon.budget.web.rest.errors.BadRequestAlertException;
import com.keyfalcon.budget.web.rest.util.HeaderUtil;
import com.keyfalcon.budget.service.dto.SubTypeDTO;
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
 * REST controller for managing SubType.
 */
@RestController
@RequestMapping("/api")
public class SubTypeResource {

    private final Logger log = LoggerFactory.getLogger(SubTypeResource.class);

    private static final String ENTITY_NAME = "subType";

    private final SubTypeService subTypeService;

    public SubTypeResource(SubTypeService subTypeService) {
        this.subTypeService = subTypeService;
    }

    /**
     * POST  /sub-types : Create a new subType.
     *
     * @param subTypeDTO the subTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subTypeDTO, or with status 400 (Bad Request) if the subType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sub-types")
    @Timed
    public ResponseEntity<SubTypeDTO> createSubType(@Valid @RequestBody SubTypeDTO subTypeDTO) throws URISyntaxException {
        log.debug("REST request to save SubType : {}", subTypeDTO);
        if (subTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new subType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubTypeDTO result = subTypeService.save(subTypeDTO);
        return ResponseEntity.created(new URI("/api/sub-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sub-types : Updates an existing subType.
     *
     * @param subTypeDTO the subTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subTypeDTO,
     * or with status 400 (Bad Request) if the subTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the subTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sub-types")
    @Timed
    public ResponseEntity<SubTypeDTO> updateSubType(@Valid @RequestBody SubTypeDTO subTypeDTO) throws URISyntaxException {
        log.debug("REST request to update SubType : {}", subTypeDTO);
        if (subTypeDTO.getId() == null) {
            return createSubType(subTypeDTO);
        }
        SubTypeDTO result = subTypeService.save(subTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sub-types : get all the subTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of subTypes in body
     */
    @GetMapping("/sub-types")
    @Timed
    public List<SubTypeDTO> getAllSubTypes() {
        log.debug("REST request to get all SubTypes");
        return subTypeService.findAll();
        }

    /**
     * GET  /sub-types/:id : get the "id" subType.
     *
     * @param id the id of the subTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sub-types/{id}")
    @Timed
    public ResponseEntity<SubTypeDTO> getSubType(@PathVariable Long id) {
        log.debug("REST request to get SubType : {}", id);
        SubTypeDTO subTypeDTO = subTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subTypeDTO));
    }

    /**
     * DELETE  /sub-types/:id : delete the "id" subType.
     *
     * @param id the id of the subTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sub-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubType(@PathVariable Long id) {
        log.debug("REST request to delete SubType : {}", id);
        subTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
