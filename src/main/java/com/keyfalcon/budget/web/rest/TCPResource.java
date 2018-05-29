package com.keyfalcon.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.keyfalcon.budget.Role;
import com.keyfalcon.budget.domain.TCP;
import com.keyfalcon.budget.service.TCPService;
import com.keyfalcon.budget.web.rest.errors.BadRequestAlertException;
import com.keyfalcon.budget.web.rest.util.HeaderUtil;
import com.keyfalcon.budget.service.dto.TCPDTO;
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
 * REST controller for managing TCP.
 */
@RestController
@RequestMapping("/api")
public class TCPResource {

    private final Logger log = LoggerFactory.getLogger(TCPResource.class);

    private static final String ENTITY_NAME = "tCP";

    private final TCPService tCPService;

    public TCPResource(TCPService tCPService) {
        this.tCPService = tCPService;
    }

    /**
     * POST  /tcps : Create a new tCP.
     *
     * @param tCPDTO the tCPDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tCPDTO, or with status 400 (Bad Request) if the tCP has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tcps")
    @Timed
    public ResponseEntity<TCPDTO> createTCP(@Valid @RequestBody TCPDTO tCPDTO) throws URISyntaxException {
        log.debug("REST request to save TCP : {}", tCPDTO);
        if (tCPDTO.getId() != null) {
            throw new BadRequestAlertException("A new tCP cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TCPDTO result = tCPService.save(tCPDTO);
        return ResponseEntity.created(new URI("/api/tcps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tcps : Updates an existing tCP.
     *
     * @param tCPDTO the tCPDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tCPDTO,
     * or with status 400 (Bad Request) if the tCPDTO is not valid,
     * or with status 500 (Internal Server Error) if the tCPDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tcps")
    @Timed
    public ResponseEntity<TCPDTO> updateTCP(@Valid @RequestBody TCPDTO tCPDTO) throws URISyntaxException {
        log.debug("REST request to update TCP : {}", tCPDTO);
        if (tCPDTO.getId() == null) {
            return createTCP(tCPDTO);
        }
        TCPDTO result = tCPService.save(tCPDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tCPDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tcps : get all the tCPS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tCPS in body
     */
    @GetMapping("/tcps")
    @Timed
    public List<TCPDTO> getAllTCPS() {
        log.debug("REST request to get all TCPS");
        return tCPService.findAll();
        }

    /**
     * GET  /tcps/:id : get the "id" tCP.
     *
     * @param id the id of the tCPDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tCPDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tcps/{id}")
    @Timed
    public ResponseEntity<TCPDTO> getTCP(@PathVariable Long id) {
        log.debug("REST request to get TCP : {}", id);
        TCPDTO tCPDTO = tCPService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tCPDTO));
    }

    /**
     * DELETE  /tcps/:id : delete the "id" tCP.
     *
     * @param id the id of the tCPDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tcps/{id}")
    @Timed
    public ResponseEntity<Void> deleteTCP(@PathVariable Long id) {
        log.debug("REST request to delete TCP : {}", id);
        tCPService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /states/filter/{userRole}/{id} : get all filtered the states.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of states in body
     */
    @GetMapping("/tcps/filter/{userRole}/{id}")
    @Timed
    public List<TCPDTO> getFilteredItems(@PathVariable Long userRole, @PathVariable Long id) {
        log.debug("REST request to get all filtered items");
        List<TCPDTO> tcpList = null;
        if(Role.getValue(userRole) == Role.SUPERADMIN) {
            tcpList = tCPService.findAll();
        } else {
            tcpList = tCPService.findAllFilteredItems(id);
        }
        return tcpList;
    }

    @GetMapping("/tcps/search/{paraNoTCP}/{userRole}/{id}")
    @Timed
    public List<TCPDTO> getSearchParaNoTCP(@PathVariable String paraNoTCP, @PathVariable Long userRole, @PathVariable Long id) {
        log.debug("REST request to get all ParaNoTCP");
        if(paraNoTCP != null) {
            if("@~all~@".equals(paraNoTCP.trim())) {
                return getFilteredItems(userRole, id);
            }
        }
        List<TCPDTO> tcpList = null;
        if(Role.getValue(userRole) == Role.SUPERADMIN) {
            tcpList = tCPService.searchAllItems(paraNoTCP);
        } else {
            tcpList = tCPService.searchItems(paraNoTCP, id);
        }
        return tcpList;
    }
}
