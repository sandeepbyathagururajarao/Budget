package com.keyfalcon.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.keyfalcon.budget.service.UserDataService;
import com.keyfalcon.budget.web.rest.errors.BadRequestAlertException;
import com.keyfalcon.budget.web.rest.util.HeaderUtil;
import com.keyfalcon.budget.service.dto.UserDataDTO;
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
 * REST controller for managing UserData.
 */
@RestController
@RequestMapping("/api")
public class UserDataResource {

    private final Logger log = LoggerFactory.getLogger(UserDataResource.class);

    private static final String ENTITY_NAME = "userData";

    private final UserDataService userDataService;

    public UserDataResource(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    /**
     * POST  /user-data : Create a new userData.
     *
     * @param userDataDTO the userDataDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userDataDTO, or with status 400 (Bad Request) if the userData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-data")
    @Timed
    public ResponseEntity<UserDataDTO> createUserData(@Valid @RequestBody UserDataDTO userDataDTO) throws URISyntaxException {
        log.debug("REST request to save UserData : {}", userDataDTO);
        if (userDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new userData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserDataDTO result = userDataService.save(userDataDTO);
        return ResponseEntity.created(new URI("/api/user-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-data : Updates an existing userData.
     *
     * @param userDataDTO the userDataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userDataDTO,
     * or with status 400 (Bad Request) if the userDataDTO is not valid,
     * or with status 500 (Internal Server Error) if the userDataDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-data")
    @Timed
    public ResponseEntity<UserDataDTO> updateUserData(@Valid @RequestBody UserDataDTO userDataDTO) throws URISyntaxException {
        log.debug("REST request to update UserData : {}", userDataDTO);
        if (userDataDTO.getId() == null) {
            return createUserData(userDataDTO);
        }
        UserDataDTO result = userDataService.save(userDataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-data : get all the userData.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userData in body
     */
    @GetMapping("/user-data")
    @Timed
    public List<UserDataDTO> getAllUserData() {
        log.debug("REST request to get all UserData");
        return userDataService.findAll();
        }

    /**
     * GET  /user-data/:id : get the "id" userData.
     *
     * @param id the id of the userDataDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userDataDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-data/{id}")
    @Timed
    public ResponseEntity<UserDataDTO> getUserData(@PathVariable Long id) {
        log.debug("REST request to get UserData : {}", id);
        UserDataDTO userDataDTO = userDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userDataDTO));
    }

    /**
     * DELETE  /user-data/:id : delete the "id" userData.
     *
     * @param id the id of the userDataDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserData(@PathVariable Long id) {
        log.debug("REST request to delete UserData : {}", id);
        userDataService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /user-data/validate/:userName : validate "userName" userData.
     *
     * @param userName the id of the userDataDTO to validate
     * @return the ResponseEntity with status 200 (OK)
     */
    @GetMapping("/user-data/validate/{userName}")
    @Timed
    public List<UserDataDTO> getUserData(@PathVariable String userName) {
        log.debug("REST request to validate against UserData : {}", userName);
        return userDataService.getUserDataByUserId(userName);
    }

    /**
     * GET  /user-data/filter/:userName : filter "userName" userData.
     *
     * @param userName the id of the userDataDTO to filter
     * @return the ResponseEntity with status 200 (OK)
     */
    @GetMapping("/user-data/filter/{userName}")
    @Timed
    public List<UserDataDTO> getCreatedUsers(@PathVariable String userName) {
        log.debug("REST request to filter against UserData : {}", userName);
        return userDataService.getCreatedUsers(userName);
    }
}
