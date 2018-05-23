package com.keyfalcon.budget.web.rest;

import com.keyfalcon.budget.BudgetApp;

import com.keyfalcon.budget.domain.Guideline;
import com.keyfalcon.budget.repository.GuidelineRepository;
import com.keyfalcon.budget.service.GuidelineService;
import com.keyfalcon.budget.service.dto.GuidelineDTO;
import com.keyfalcon.budget.service.mapper.GuidelineMapper;
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
 * Test class for the GuidelineResource REST controller.
 *
 * @see GuidelineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class GuidelineResourceIntTest {

    private static final String DEFAULT_PARA_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARA_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private GuidelineRepository guidelineRepository;

    @Autowired
    private GuidelineMapper guidelineMapper;

    @Autowired
    private GuidelineService guidelineService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGuidelineMockMvc;

    private Guideline guideline;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GuidelineResource guidelineResource = new GuidelineResource(guidelineService);
        this.restGuidelineMockMvc = MockMvcBuilders.standaloneSetup(guidelineResource)
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
    public static Guideline createEntity(EntityManager em) {
        Guideline guideline = new Guideline()
            .paraName(DEFAULT_PARA_NAME)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return guideline;
    }

    @Before
    public void initTest() {
        guideline = createEntity(em);
    }

    @Test
    @Transactional
    public void createGuideline() throws Exception {
        int databaseSizeBeforeCreate = guidelineRepository.findAll().size();

        // Create the Guideline
        GuidelineDTO guidelineDTO = guidelineMapper.toDto(guideline);
        restGuidelineMockMvc.perform(post("/api/guidelines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guidelineDTO)))
            .andExpect(status().isCreated());

        // Validate the Guideline in the database
        List<Guideline> guidelineList = guidelineRepository.findAll();
        assertThat(guidelineList).hasSize(databaseSizeBeforeCreate + 1);
        Guideline testGuideline = guidelineList.get(guidelineList.size() - 1);
        assertThat(testGuideline.getParaName()).isEqualTo(DEFAULT_PARA_NAME);
        assertThat(testGuideline.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testGuideline.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createGuidelineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = guidelineRepository.findAll().size();

        // Create the Guideline with an existing ID
        guideline.setId(1L);
        GuidelineDTO guidelineDTO = guidelineMapper.toDto(guideline);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuidelineMockMvc.perform(post("/api/guidelines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guidelineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Guideline in the database
        List<Guideline> guidelineList = guidelineRepository.findAll();
        assertThat(guidelineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkParaNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = guidelineRepository.findAll().size();
        // set the field null
        guideline.setParaName(null);

        // Create the Guideline, which fails.
        GuidelineDTO guidelineDTO = guidelineMapper.toDto(guideline);

        restGuidelineMockMvc.perform(post("/api/guidelines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guidelineDTO)))
            .andExpect(status().isBadRequest());

        List<Guideline> guidelineList = guidelineRepository.findAll();
        assertThat(guidelineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = guidelineRepository.findAll().size();
        // set the field null
        guideline.setCreatedDate(null);

        // Create the Guideline, which fails.
        GuidelineDTO guidelineDTO = guidelineMapper.toDto(guideline);

        restGuidelineMockMvc.perform(post("/api/guidelines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guidelineDTO)))
            .andExpect(status().isBadRequest());

        List<Guideline> guidelineList = guidelineRepository.findAll();
        assertThat(guidelineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGuidelines() throws Exception {
        // Initialize the database
        guidelineRepository.saveAndFlush(guideline);

        // Get all the guidelineList
        restGuidelineMockMvc.perform(get("/api/guidelines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guideline.getId().intValue())))
            .andExpect(jsonPath("$.[*].paraName").value(hasItem(DEFAULT_PARA_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    public void getGuideline() throws Exception {
        // Initialize the database
        guidelineRepository.saveAndFlush(guideline);

        // Get the guideline
        restGuidelineMockMvc.perform(get("/api/guidelines/{id}", guideline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(guideline.getId().intValue()))
            .andExpect(jsonPath("$.paraName").value(DEFAULT_PARA_NAME.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingGuideline() throws Exception {
        // Get the guideline
        restGuidelineMockMvc.perform(get("/api/guidelines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuideline() throws Exception {
        // Initialize the database
        guidelineRepository.saveAndFlush(guideline);
        int databaseSizeBeforeUpdate = guidelineRepository.findAll().size();

        // Update the guideline
        Guideline updatedGuideline = guidelineRepository.findOne(guideline.getId());
        // Disconnect from session so that the updates on updatedGuideline are not directly saved in db
        em.detach(updatedGuideline);
        updatedGuideline
            .paraName(UPDATED_PARA_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        GuidelineDTO guidelineDTO = guidelineMapper.toDto(updatedGuideline);

        restGuidelineMockMvc.perform(put("/api/guidelines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guidelineDTO)))
            .andExpect(status().isOk());

        // Validate the Guideline in the database
        List<Guideline> guidelineList = guidelineRepository.findAll();
        assertThat(guidelineList).hasSize(databaseSizeBeforeUpdate);
        Guideline testGuideline = guidelineList.get(guidelineList.size() - 1);
        assertThat(testGuideline.getParaName()).isEqualTo(UPDATED_PARA_NAME);
        assertThat(testGuideline.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testGuideline.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingGuideline() throws Exception {
        int databaseSizeBeforeUpdate = guidelineRepository.findAll().size();

        // Create the Guideline
        GuidelineDTO guidelineDTO = guidelineMapper.toDto(guideline);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGuidelineMockMvc.perform(put("/api/guidelines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guidelineDTO)))
            .andExpect(status().isCreated());

        // Validate the Guideline in the database
        List<Guideline> guidelineList = guidelineRepository.findAll();
        assertThat(guidelineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGuideline() throws Exception {
        // Initialize the database
        guidelineRepository.saveAndFlush(guideline);
        int databaseSizeBeforeDelete = guidelineRepository.findAll().size();

        // Get the guideline
        restGuidelineMockMvc.perform(delete("/api/guidelines/{id}", guideline.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Guideline> guidelineList = guidelineRepository.findAll();
        assertThat(guidelineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Guideline.class);
        Guideline guideline1 = new Guideline();
        guideline1.setId(1L);
        Guideline guideline2 = new Guideline();
        guideline2.setId(guideline1.getId());
        assertThat(guideline1).isEqualTo(guideline2);
        guideline2.setId(2L);
        assertThat(guideline1).isNotEqualTo(guideline2);
        guideline1.setId(null);
        assertThat(guideline1).isNotEqualTo(guideline2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuidelineDTO.class);
        GuidelineDTO guidelineDTO1 = new GuidelineDTO();
        guidelineDTO1.setId(1L);
        GuidelineDTO guidelineDTO2 = new GuidelineDTO();
        assertThat(guidelineDTO1).isNotEqualTo(guidelineDTO2);
        guidelineDTO2.setId(guidelineDTO1.getId());
        assertThat(guidelineDTO1).isEqualTo(guidelineDTO2);
        guidelineDTO2.setId(2L);
        assertThat(guidelineDTO1).isNotEqualTo(guidelineDTO2);
        guidelineDTO1.setId(null);
        assertThat(guidelineDTO1).isNotEqualTo(guidelineDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(guidelineMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(guidelineMapper.fromId(null)).isNull();
    }
}
