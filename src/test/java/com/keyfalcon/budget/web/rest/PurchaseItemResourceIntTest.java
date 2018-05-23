package com.keyfalcon.budget.web.rest;

import com.keyfalcon.budget.BudgetApp;

import com.keyfalcon.budget.domain.PurchaseItem;
import com.keyfalcon.budget.repository.PurchaseItemRepository;
import com.keyfalcon.budget.service.PurchaseItemService;
import com.keyfalcon.budget.service.dto.PurchaseItemDTO;
import com.keyfalcon.budget.service.mapper.PurchaseItemMapper;
import com.keyfalcon.budget.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.keyfalcon.budget.web.rest.TestUtil.sameInstant;
import static com.keyfalcon.budget.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PurchaseItemResource REST controller.
 *
 * @see PurchaseItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class PurchaseItemResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_GPS_COORDINATE = "AAAAAAAAAA";
    private static final String UPDATED_GPS_COORDINATE = "BBBBBBBBBB";

    private static final String DEFAULT_JUSTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_APPROVAL_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_APPROVAL_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private PurchaseItemRepository purchaseItemRepository;

    @Autowired
    private PurchaseItemMapper purchaseItemMapper;

    @Autowired
    private PurchaseItemService purchaseItemService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPurchaseItemMockMvc;

    private PurchaseItem purchaseItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseItemResource purchaseItemResource = new PurchaseItemResource(purchaseItemService);
        this.restPurchaseItemMockMvc = MockMvcBuilders.standaloneSetup(purchaseItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseItem createEntity(EntityManager em) {
        PurchaseItem purchaseItem = new PurchaseItem()
            .type(DEFAULT_TYPE)
            .gpsCoordinate(DEFAULT_GPS_COORDINATE)
            .justification(DEFAULT_JUSTIFICATION)
            .image(DEFAULT_IMAGE)
            .approvalStatus(DEFAULT_APPROVAL_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return purchaseItem;
    }

    @Before
    public void initTest() {
        purchaseItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchaseItem() throws Exception {
        int databaseSizeBeforeCreate = purchaseItemRepository.findAll().size();

        // Create the PurchaseItem
        PurchaseItemDTO purchaseItemDTO = purchaseItemMapper.toDto(purchaseItem);
        restPurchaseItemMockMvc.perform(post("/api/purchase-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseItemDTO)))
            .andExpect(status().isCreated());

        // Validate the PurchaseItem in the database
        List<PurchaseItem> purchaseItemList = purchaseItemRepository.findAll();
        assertThat(purchaseItemList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseItem testPurchaseItem = purchaseItemList.get(purchaseItemList.size() - 1);
        assertThat(testPurchaseItem.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPurchaseItem.getGpsCoordinate()).isEqualTo(DEFAULT_GPS_COORDINATE);
        assertThat(testPurchaseItem.getJustification()).isEqualTo(DEFAULT_JUSTIFICATION);
        assertThat(testPurchaseItem.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPurchaseItem.getApprovalStatus()).isEqualTo(DEFAULT_APPROVAL_STATUS);
        assertThat(testPurchaseItem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPurchaseItem.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createPurchaseItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseItemRepository.findAll().size();

        // Create the PurchaseItem with an existing ID
        purchaseItem.setId(1L);
        PurchaseItemDTO purchaseItemDTO = purchaseItemMapper.toDto(purchaseItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseItemMockMvc.perform(post("/api/purchase-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseItem in the database
        List<PurchaseItem> purchaseItemList = purchaseItemRepository.findAll();
        assertThat(purchaseItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseItemRepository.findAll().size();
        // set the field null
        purchaseItem.setType(null);

        // Create the PurchaseItem, which fails.
        PurchaseItemDTO purchaseItemDTO = purchaseItemMapper.toDto(purchaseItem);

        restPurchaseItemMockMvc.perform(post("/api/purchase-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseItemDTO)))
            .andExpect(status().isBadRequest());

        List<PurchaseItem> purchaseItemList = purchaseItemRepository.findAll();
        assertThat(purchaseItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApprovalStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseItemRepository.findAll().size();
        // set the field null
        purchaseItem.setApprovalStatus(null);

        // Create the PurchaseItem, which fails.
        PurchaseItemDTO purchaseItemDTO = purchaseItemMapper.toDto(purchaseItem);

        restPurchaseItemMockMvc.perform(post("/api/purchase-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseItemDTO)))
            .andExpect(status().isBadRequest());

        List<PurchaseItem> purchaseItemList = purchaseItemRepository.findAll();
        assertThat(purchaseItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseItemRepository.findAll().size();
        // set the field null
        purchaseItem.setCreatedDate(null);

        // Create the PurchaseItem, which fails.
        PurchaseItemDTO purchaseItemDTO = purchaseItemMapper.toDto(purchaseItem);

        restPurchaseItemMockMvc.perform(post("/api/purchase-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseItemDTO)))
            .andExpect(status().isBadRequest());

        List<PurchaseItem> purchaseItemList = purchaseItemRepository.findAll();
        assertThat(purchaseItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPurchaseItems() throws Exception {
        // Initialize the database
        purchaseItemRepository.saveAndFlush(purchaseItem);

        // Get all the purchaseItemList
        restPurchaseItemMockMvc.perform(get("/api/purchase-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].gpsCoordinate").value(hasItem(DEFAULT_GPS_COORDINATE.toString())))
            .andExpect(jsonPath("$.[*].justification").value(hasItem(DEFAULT_JUSTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].approvalStatus").value(hasItem(DEFAULT_APPROVAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    public void getPurchaseItem() throws Exception {
        // Initialize the database
        purchaseItemRepository.saveAndFlush(purchaseItem);

        // Get the purchaseItem
        restPurchaseItemMockMvc.perform(get("/api/purchase-items/{id}", purchaseItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseItem.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.gpsCoordinate").value(DEFAULT_GPS_COORDINATE.toString()))
            .andExpect(jsonPath("$.justification").value(DEFAULT_JUSTIFICATION.toString()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()))
            .andExpect(jsonPath("$.approvalStatus").value(DEFAULT_APPROVAL_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingPurchaseItem() throws Exception {
        // Get the purchaseItem
        restPurchaseItemMockMvc.perform(get("/api/purchase-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseItem() throws Exception {
        // Initialize the database
        purchaseItemRepository.saveAndFlush(purchaseItem);
        int databaseSizeBeforeUpdate = purchaseItemRepository.findAll().size();

        // Update the purchaseItem
        PurchaseItem updatedPurchaseItem = purchaseItemRepository.findOne(purchaseItem.getId());
        // Disconnect from session so that the updates on updatedPurchaseItem are not directly saved in db
        em.detach(updatedPurchaseItem);
        updatedPurchaseItem
            .type(UPDATED_TYPE)
            .gpsCoordinate(UPDATED_GPS_COORDINATE)
            .justification(UPDATED_JUSTIFICATION)
            .image(UPDATED_IMAGE)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        PurchaseItemDTO purchaseItemDTO = purchaseItemMapper.toDto(updatedPurchaseItem);

        restPurchaseItemMockMvc.perform(put("/api/purchase-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseItemDTO)))
            .andExpect(status().isOk());

        // Validate the PurchaseItem in the database
        List<PurchaseItem> purchaseItemList = purchaseItemRepository.findAll();
        assertThat(purchaseItemList).hasSize(databaseSizeBeforeUpdate);
        PurchaseItem testPurchaseItem = purchaseItemList.get(purchaseItemList.size() - 1);
        assertThat(testPurchaseItem.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPurchaseItem.getGpsCoordinate()).isEqualTo(UPDATED_GPS_COORDINATE);
        assertThat(testPurchaseItem.getJustification()).isEqualTo(UPDATED_JUSTIFICATION);
        assertThat(testPurchaseItem.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPurchaseItem.getApprovalStatus()).isEqualTo(UPDATED_APPROVAL_STATUS);
        assertThat(testPurchaseItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchaseItem.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchaseItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseItemRepository.findAll().size();

        // Create the PurchaseItem
        PurchaseItemDTO purchaseItemDTO = purchaseItemMapper.toDto(purchaseItem);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPurchaseItemMockMvc.perform(put("/api/purchase-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseItemDTO)))
            .andExpect(status().isCreated());

        // Validate the PurchaseItem in the database
        List<PurchaseItem> purchaseItemList = purchaseItemRepository.findAll();
        assertThat(purchaseItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePurchaseItem() throws Exception {
        // Initialize the database
        purchaseItemRepository.saveAndFlush(purchaseItem);
        int databaseSizeBeforeDelete = purchaseItemRepository.findAll().size();

        // Get the purchaseItem
        restPurchaseItemMockMvc.perform(delete("/api/purchase-items/{id}", purchaseItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PurchaseItem> purchaseItemList = purchaseItemRepository.findAll();
        assertThat(purchaseItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseItem.class);
        PurchaseItem purchaseItem1 = new PurchaseItem();
        purchaseItem1.setId(1L);
        PurchaseItem purchaseItem2 = new PurchaseItem();
        purchaseItem2.setId(purchaseItem1.getId());
        assertThat(purchaseItem1).isEqualTo(purchaseItem2);
        purchaseItem2.setId(2L);
        assertThat(purchaseItem1).isNotEqualTo(purchaseItem2);
        purchaseItem1.setId(null);
        assertThat(purchaseItem1).isNotEqualTo(purchaseItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseItemDTO.class);
        PurchaseItemDTO purchaseItemDTO1 = new PurchaseItemDTO();
        purchaseItemDTO1.setId(1L);
        PurchaseItemDTO purchaseItemDTO2 = new PurchaseItemDTO();
        assertThat(purchaseItemDTO1).isNotEqualTo(purchaseItemDTO2);
        purchaseItemDTO2.setId(purchaseItemDTO1.getId());
        assertThat(purchaseItemDTO1).isEqualTo(purchaseItemDTO2);
        purchaseItemDTO2.setId(2L);
        assertThat(purchaseItemDTO1).isNotEqualTo(purchaseItemDTO2);
        purchaseItemDTO1.setId(null);
        assertThat(purchaseItemDTO1).isNotEqualTo(purchaseItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(purchaseItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(purchaseItemMapper.fromId(null)).isNull();
    }
}
