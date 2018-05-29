package com.keyfalcon.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.keyfalcon.budget.Role;
import com.keyfalcon.budget.service.GuidelineService;
import com.keyfalcon.budget.web.rest.errors.BadRequestAlertException;
import com.keyfalcon.budget.web.rest.util.HeaderUtil;
import com.keyfalcon.budget.service.dto.GuidelineDTO;
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
 * REST controller for managing Guideline.
 */
@RestController
@RequestMapping("/api")
public class GuidelineResource {

    private final Logger log = LoggerFactory.getLogger(GuidelineResource.class);

    private static final String ENTITY_NAME = "guideline";

    private final GuidelineService guidelineService;

    public GuidelineResource(GuidelineService guidelineService) {
        this.guidelineService = guidelineService;
    }

    /**
     * POST  /guidelines : Create a new guideline.
     *
     * @param guidelineDTO the guidelineDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new guidelineDTO, or with status 400 (Bad Request) if the guideline has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/guidelines")
    @Timed
    public ResponseEntity<GuidelineDTO> createGuideline(@Valid @RequestBody GuidelineDTO guidelineDTO) throws URISyntaxException {
        log.debug("REST request to save Guideline : {}", guidelineDTO);
        if (guidelineDTO.getId() != null) {
            throw new BadRequestAlertException("A new guideline cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuidelineDTO result = guidelineService.save(guidelineDTO);
        return ResponseEntity.created(new URI("/api/guidelines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /guidelines : Updates an existing guideline.
     *
     * @param guidelineDTO the guidelineDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated guidelineDTO,
     * or with status 400 (Bad Request) if the guidelineDTO is not valid,
     * or with status 500 (Internal Server Error) if the guidelineDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/guidelines")
    @Timed
    public ResponseEntity<GuidelineDTO> updateGuideline(@Valid @RequestBody GuidelineDTO guidelineDTO) throws URISyntaxException {
        log.debug("REST request to update Guideline : {}", guidelineDTO);
        if (guidelineDTO.getId() == null) {
            return createGuideline(guidelineDTO);
        }
        GuidelineDTO result = guidelineService.save(guidelineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, guidelineDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /guidelines : get all the guidelines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of guidelines in body
     */
    @GetMapping("/guidelines")
    @Timed
    public List<GuidelineDTO> getAllGuidelines() {
        log.debug("REST request to get all Guidelines");
        return guidelineService.findAll();
        }

    /**
     * GET  /guidelines/:id : get the "id" guideline.
     *
     * @param id the id of the guidelineDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the guidelineDTO, or with status 404 (Not Found)
     */
    @GetMapping("/guidelines/{id}")
    @Timed
    public ResponseEntity<GuidelineDTO> getGuideline(@PathVariable Long id) {
        log.debug("REST request to get Guideline : {}", id);
        GuidelineDTO guidelineDTO = guidelineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(guidelineDTO));
    }

    /**
     * DELETE  /guidelines/:id : delete the "id" guideline.
     *
     * @param id the id of the guidelineDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/guidelines/{id}")
    @Timed
    public ResponseEntity<Void> deleteGuideline(@PathVariable Long id) {
        log.debug("REST request to delete Guideline : {}", id);
        guidelineService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /states/filter/{userRole}/{id} : get all filtered the states.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of states in body
     */
    @GetMapping("/guidelines/filter/{userRole}/{id}")
    @Timed
    public List<GuidelineDTO> getFilteredGuidelines(@PathVariable Long userRole, @PathVariable Long id) {
        log.debug("REST request to get all filtered guidelines");
        List<GuidelineDTO> guidelineDTOList = null;
        if(Role.getValue(userRole) == Role.SUPERADMIN) {
            guidelineDTOList = guidelineService.findAll();
        } else {
            guidelineDTOList = guidelineService.findAllFilteredGuidelines(id);
        }
        return guidelineDTOList;
    }

    @GetMapping("/guidelines/search/{paraName}/{userRole}/{id}")
    @Timed
    public List<GuidelineDTO> getSearchGuidelines(@PathVariable String paraName, @PathVariable Long userRole, @PathVariable Long id) {
        log.debug("REST request to get all searched guidelines");
        if(paraName != null) {
            if("@~all~@".equals(paraName.trim())) {
                return getFilteredGuidelines(userRole, id);
            }
        }
        List<GuidelineDTO> guidelineDTOList = null;
        if(Role.getValue(userRole) == Role.SUPERADMIN) {
            guidelineDTOList = guidelineService.searchAllItems(paraName);
        } else {
            guidelineDTOList = guidelineService.searchItems(paraName, id);
        }
        return guidelineDTOList;
    }
}
