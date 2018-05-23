package com.keyfalcon.budget.web.rest;

import com.keyfalcon.budget.BudgetApp;

import com.keyfalcon.budget.domain.PurchaseSubItem;
import com.keyfalcon.budget.repository.PurchaseSubItemRepository;
import com.keyfalcon.budget.service.PurchaseSubItemService;
import com.keyfalcon.budget.service.dto.PurchaseSubItemDTO;
import com.keyfalcon.budget.service.mapper.PurchaseSubItemMapper;
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
 * Test class for the PurchaseSubItemResource REST controller.
 *
 * @see PurchaseSubItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class PurchaseSubItemResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NO_OF_ITEM = "AAAAAAAAAA";
    private static final String UPDATED_NO_OF_ITEM = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_PRICE = "BBBBBBBBBB";

    private static final String DEFAULT_TOTAL = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private PurchaseSubItemRepository purchaseSubItemRepository;

    @Autowired
    private PurchaseSubItemMapper purchaseSubItemMapper;

    @Autowired
    private PurchaseSubItemService purchaseSubItemService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPurchaseSubItemMockMvc;

    private PurchaseSubItem purchaseSubItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseSubItemResource purchaseSubItemResource = new PurchaseSubItemResource(purchaseSubItemService);
        this.restPurchaseSubItemMockMvc = MockMvcBuilders.standaloneSetup(purchaseSubItemResource)
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
    public static PurchaseSubItem createEntity(EntityManager em) {
        PurchaseSubItem purchaseSubItem = new PurchaseSubItem()
            .name(DEFAULT_NAME)
            .noOfItem(DEFAULT_NO_OF_ITEM)
            .unit(DEFAULT_UNIT)
            .price(DEFAULT_PRICE)
            .total(DEFAULT_TOTAL)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return purchaseSubItem;
    }

    @Before
    public void initTest() {
        purchaseSubItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchaseSubItem() throws Exception {
        int databaseSizeBeforeCreate = purchaseSubItemRepository.findAll().size();

        // Create the PurchaseSubItem
        PurchaseSubItemDTO purchaseSubItemDTO = purchaseSubItemMapper.toDto(purchaseSubItem);
        restPurchaseSubItemMockMvc.perform(post("/api/purchase-sub-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseSubItemDTO)))
            .andExpect(status().isCreated());

        // Validate the PurchaseSubItem in the database
        List<PurchaseSubItem> purchaseSubItemList = purchaseSubItemRepository.findAll();
        assertThat(purchaseSubItemList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseSubItem testPurchaseSubItem = purchaseSubItemList.get(purchaseSubItemList.size() - 1);
        assertThat(testPurchaseSubItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPurchaseSubItem.getNoOfItem()).isEqualTo(DEFAULT_NO_OF_ITEM);
        assertThat(testPurchaseSubItem.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testPurchaseSubItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testPurchaseSubItem.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testPurchaseSubItem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPurchaseSubItem.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createPurchaseSubItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseSubItemRepository.findAll().size();

        // Create the PurchaseSubItem with an existing ID
        purchaseSubItem.setId(1L);
        PurchaseSubItemDTO purchaseSubItemDTO = purchaseSubItemMapper.toDto(purchaseSubItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseSubItemMockMvc.perform(post("/api/purchase-sub-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseSubItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseSubItem in the database
        List<PurchaseSubItem> purchaseSubItemList = purchaseSubItemRepository.findAll();
        assertThat(purchaseSubItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseSubItemRepository.findAll().size();
        // set the field null
        purchaseSubItem.setTotal(null);

        // Create the PurchaseSubItem, which fails.
        PurchaseSubItemDTO purchaseSubItemDTO = purchaseSubItemMapper.toDto(purchaseSubItem);

        restPurchaseSubItemMockMvc.perform(post("/api/purchase-sub-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseSubItemDTO)))
            .andExpect(status().isBadRequest());

        List<PurchaseSubItem> purchaseSubItemList = purchaseSubItemRepository.findAll();
        assertThat(purchaseSubItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseSubItemRepository.findAll().size();
        // set the field null
        purchaseSubItem.setCreatedDate(null);

        // Create the PurchaseSubItem, which fails.
        PurchaseSubItemDTO purchaseSubItemDTO = purchaseSubItemMapper.toDto(purchaseSubItem);

        restPurchaseSubItemMockMvc.perform(post("/api/purchase-sub-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseSubItemDTO)))
            .andExpect(status().isBadRequest());

        List<PurchaseSubItem> purchaseSubItemList = purchaseSubItemRepository.findAll();
        assertThat(purchaseSubItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPurchaseSubItems() throws Exception {
        // Initialize the database
        purchaseSubItemRepository.saveAndFlush(purchaseSubItem);

        // Get all the purchaseSubItemList
        restPurchaseSubItemMockMvc.perform(get("/api/purchase-sub-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseSubItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].noOfItem").value(hasItem(DEFAULT_NO_OF_ITEM.toString())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    public void getPurchaseSubItem() throws Exception {
        // Initialize the database
        purchaseSubItemRepository.saveAndFlush(purchaseSubItem);

        // Get the purchaseSubItem
        restPurchaseSubItemMockMvc.perform(get("/api/purchase-sub-items/{id}", purchaseSubItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseSubItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.noOfItem").value(DEFAULT_NO_OF_ITEM.toString()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingPurchaseSubItem() throws Exception {
        // Get the purchaseSubItem
        restPurchaseSubItemMockMvc.perform(get("/api/purchase-sub-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseSubItem() throws Exception {
        // Initialize the database
        purchaseSubItemRepository.saveAndFlush(purchaseSubItem);
        int databaseSizeBeforeUpdate = purchaseSubItemRepository.findAll().size();

        // Update the purchaseSubItem
        PurchaseSubItem updatedPurchaseSubItem = purchaseSubItemRepository.findOne(purchaseSubItem.getId());
        // Disconnect from session so that the updates on updatedPurchaseSubItem are not directly saved in db
        em.detach(updatedPurchaseSubItem);
        updatedPurchaseSubItem
            .name(UPDATED_NAME)
            .noOfItem(UPDATED_NO_OF_ITEM)
            .unit(UPDATED_UNIT)
            .price(UPDATED_PRICE)
            .total(UPDATED_TOTAL)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        PurchaseSubItemDTO purchaseSubItemDTO = purchaseSubItemMapper.toDto(updatedPurchaseSubItem);

        restPurchaseSubItemMockMvc.perform(put("/api/purchase-sub-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseSubItemDTO)))
            .andExpect(status().isOk());

        // Validate the PurchaseSubItem in the database
        List<PurchaseSubItem> purchaseSubItemList = purchaseSubItemRepository.findAll();
        assertThat(purchaseSubItemList).hasSize(databaseSizeBeforeUpdate);
        PurchaseSubItem testPurchaseSubItem = purchaseSubItemList.get(purchaseSubItemList.size() - 1);
        assertThat(testPurchaseSubItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPurchaseSubItem.getNoOfItem()).isEqualTo(UPDATED_NO_OF_ITEM);
        assertThat(testPurchaseSubItem.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testPurchaseSubItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testPurchaseSubItem.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testPurchaseSubItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchaseSubItem.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchaseSubItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseSubItemRepository.findAll().size();

        // Create the PurchaseSubItem
        PurchaseSubItemDTO purchaseSubItemDTO = purchaseSubItemMapper.toDto(purchaseSubItem);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPurchaseSubItemMockMvc.perform(put("/api/purchase-sub-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseSubItemDTO)))
            .andExpect(status().isCreated());

        // Validate the PurchaseSubItem in the database
        List<PurchaseSubItem> purchaseSubItemList = purchaseSubItemRepository.findAll();
        assertThat(purchaseSubItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePurchaseSubItem() throws Exception {
        // Initialize the database
        purchaseSubItemRepository.saveAndFlush(purchaseSubItem);
        int databaseSizeBeforeDelete = purchaseSubItemRepository.findAll().size();

        // Get the purchaseSubItem
        restPurchaseSubItemMockMvc.perform(delete("/api/purchase-sub-items/{id}", purchaseSubItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PurchaseSubItem> purchaseSubItemList = purchaseSubItemRepository.findAll();
        assertThat(purchaseSubItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseSubItem.class);
        PurchaseSubItem purchaseSubItem1 = new PurchaseSubItem();
        purchaseSubItem1.setId(1L);
        PurchaseSubItem purchaseSubItem2 = new PurchaseSubItem();
        purchaseSubItem2.setId(purchaseSubItem1.getId());
        assertThat(purchaseSubItem1).isEqualTo(purchaseSubItem2);
        purchaseSubItem2.setId(2L);
        assertThat(purchaseSubItem1).isNotEqualTo(purchaseSubItem2);
        purchaseSubItem1.setId(null);
        assertThat(purchaseSubItem1).isNotEqualTo(purchaseSubItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseSubItemDTO.class);
        PurchaseSubItemDTO purchaseSubItemDTO1 = new PurchaseSubItemDTO();
        purchaseSubItemDTO1.setId(1L);
        PurchaseSubItemDTO purchaseSubItemDTO2 = new PurchaseSubItemDTO();
        assertThat(purchaseSubItemDTO1).isNotEqualTo(purchaseSubItemDTO2);
        purchaseSubItemDTO2.setId(purchaseSubItemDTO1.getId());
        assertThat(purchaseSubItemDTO1).isEqualTo(purchaseSubItemDTO2);
        purchaseSubItemDTO2.setId(2L);
        assertThat(purchaseSubItemDTO1).isNotEqualTo(purchaseSubItemDTO2);
        purchaseSubItemDTO1.setId(null);
        assertThat(purchaseSubItemDTO1).isNotEqualTo(purchaseSubItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(purchaseSubItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(purchaseSubItemMapper.fromId(null)).isNull();
    }
}
