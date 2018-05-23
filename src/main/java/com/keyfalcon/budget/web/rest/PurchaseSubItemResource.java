package com.keyfalcon.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.keyfalcon.budget.service.PurchaseSubItemService;
import com.keyfalcon.budget.web.rest.errors.BadRequestAlertException;
import com.keyfalcon.budget.web.rest.util.HeaderUtil;
import com.keyfalcon.budget.service.dto.PurchaseSubItemDTO;
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
 * REST controller for managing PurchaseSubItem.
 */
@RestController
@RequestMapping("/api")
public class PurchaseSubItemResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseSubItemResource.class);

    private static final String ENTITY_NAME = "purchaseSubItem";

    private final PurchaseSubItemService purchaseSubItemService;

    public PurchaseSubItemResource(PurchaseSubItemService purchaseSubItemService) {
        this.purchaseSubItemService = purchaseSubItemService;
    }

    /**
     * POST  /purchase-sub-items : Create a new purchaseSubItem.
     *
     * @param purchaseSubItemDTO the purchaseSubItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new purchaseSubItemDTO, or with status 400 (Bad Request) if the purchaseSubItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/purchase-sub-items")
    @Timed
    public ResponseEntity<PurchaseSubItemDTO> createPurchaseSubItem(@Valid @RequestBody PurchaseSubItemDTO purchaseSubItemDTO) throws URISyntaxException {
        log.debug("REST request to save PurchaseSubItem : {}", purchaseSubItemDTO);
        if (purchaseSubItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchaseSubItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseSubItemDTO result = purchaseSubItemService.save(purchaseSubItemDTO);
        return ResponseEntity.created(new URI("/api/purchase-sub-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /purchase-sub-items : Updates an existing purchaseSubItem.
     *
     * @param purchaseSubItemDTO the purchaseSubItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated purchaseSubItemDTO,
     * or with status 400 (Bad Request) if the purchaseSubItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the purchaseSubItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/purchase-sub-items")
    @Timed
    public ResponseEntity<PurchaseSubItemDTO> updatePurchaseSubItem(@Valid @RequestBody PurchaseSubItemDTO purchaseSubItemDTO) throws URISyntaxException {
        log.debug("REST request to update PurchaseSubItem : {}", purchaseSubItemDTO);
        if (purchaseSubItemDTO.getId() == null) {
            return createPurchaseSubItem(purchaseSubItemDTO);
        }
        PurchaseSubItemDTO result = purchaseSubItemService.save(purchaseSubItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, purchaseSubItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchase-sub-items : get all the purchaseSubItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of purchaseSubItems in body
     */
    @GetMapping("/purchase-sub-items")
    @Timed
    public List<PurchaseSubItemDTO> getAllPurchaseSubItems() {
        log.debug("REST request to get all PurchaseSubItems");
        return purchaseSubItemService.findAll();
        }

    /**
     * GET  /purchase-sub-items/:id : get the "id" purchaseSubItem.
     *
     * @param id the id of the purchaseSubItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the purchaseSubItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/purchase-sub-items/{id}")
    @Timed
    public ResponseEntity<PurchaseSubItemDTO> getPurchaseSubItem(@PathVariable Long id) {
        log.debug("REST request to get PurchaseSubItem : {}", id);
        PurchaseSubItemDTO purchaseSubItemDTO = purchaseSubItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(purchaseSubItemDTO));
    }

    /**
     * DELETE  /purchase-sub-items/:id : delete the "id" purchaseSubItem.
     *
     * @param id the id of the purchaseSubItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/purchase-sub-items/{id}")
    @Timed
    public ResponseEntity<Void> deletePurchaseSubItem(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseSubItem : {}", id);
        purchaseSubItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
