package com.keyfalcon.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.keyfalcon.budget.service.PurchaseItemService;
import com.keyfalcon.budget.web.rest.errors.BadRequestAlertException;
import com.keyfalcon.budget.web.rest.util.HeaderUtil;
import com.keyfalcon.budget.service.dto.PurchaseItemDTO;
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
 * REST controller for managing PurchaseItem.
 */
@RestController
@RequestMapping("/api")
public class PurchaseItemResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseItemResource.class);

    private static final String ENTITY_NAME = "purchaseItem";

    private final PurchaseItemService purchaseItemService;

    public PurchaseItemResource(PurchaseItemService purchaseItemService) {
        this.purchaseItemService = purchaseItemService;
    }

    /**
     * POST  /purchase-items : Create a new purchaseItem.
     *
     * @param purchaseItemDTO the purchaseItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new purchaseItemDTO, or with status 400 (Bad Request) if the purchaseItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/purchase-items")
    @Timed
    public ResponseEntity<PurchaseItemDTO> createPurchaseItem(@Valid @RequestBody PurchaseItemDTO purchaseItemDTO) throws URISyntaxException {
        log.debug("REST request to save PurchaseItem : {}", purchaseItemDTO);
        if (purchaseItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchaseItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseItemDTO result = purchaseItemService.save(purchaseItemDTO);
        return ResponseEntity.created(new URI("/api/purchase-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /purchase-items : Updates an existing purchaseItem.
     *
     * @param purchaseItemDTO the purchaseItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated purchaseItemDTO,
     * or with status 400 (Bad Request) if the purchaseItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the purchaseItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/purchase-items")
    @Timed
    public ResponseEntity<PurchaseItemDTO> updatePurchaseItem(@Valid @RequestBody PurchaseItemDTO purchaseItemDTO) throws URISyntaxException {
        log.debug("REST request to update PurchaseItem : {}", purchaseItemDTO);
        if (purchaseItemDTO.getId() == null) {
            return createPurchaseItem(purchaseItemDTO);
        }
        PurchaseItemDTO result = purchaseItemService.save(purchaseItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, purchaseItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchase-items : get all the purchaseItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of purchaseItems in body
     */
    @GetMapping("/purchase-items")
    @Timed
    public List<PurchaseItemDTO> getAllPurchaseItems() {
        log.debug("REST request to get all PurchaseItems");
        return purchaseItemService.findAll();
        }

    /**
     * GET  /purchase-items/:id : get the "id" purchaseItem.
     *
     * @param id the id of the purchaseItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the purchaseItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/purchase-items/{id}")
    @Timed
    public ResponseEntity<PurchaseItemDTO> getPurchaseItem(@PathVariable Long id) {
        log.debug("REST request to get PurchaseItem : {}", id);
        PurchaseItemDTO purchaseItemDTO = purchaseItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(purchaseItemDTO));
    }

    /**
     * DELETE  /purchase-items/:id : delete the "id" purchaseItem.
     *
     * @param id the id of the purchaseItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/purchase-items/{id}")
    @Timed
    public ResponseEntity<Void> deletePurchaseItem(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseItem : {}", id);
        purchaseItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
