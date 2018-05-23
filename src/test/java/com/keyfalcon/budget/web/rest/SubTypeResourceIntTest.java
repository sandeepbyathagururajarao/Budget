package com.keyfalcon.budget.web.rest;

import com.keyfalcon.budget.BudgetApp;

import com.keyfalcon.budget.domain.SubType;
import com.keyfalcon.budget.repository.SubTypeRepository;
import com.keyfalcon.budget.service.SubTypeService;
import com.keyfalcon.budget.service.dto.SubTypeDTO;
import com.keyfalcon.budget.service.mapper.SubTypeMapper;
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
 * Test class for the SubTypeResource REST controller.
 *
 * @see SubTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class SubTypeResourceIntTest {

    private static final String DEFAULT_SUB_TYPE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TYPE_NUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SubTypeRepository subTypeRepository;

    @Autowired
    private SubTypeMapper subTypeMapper;

    @Autowired
    private SubTypeService subTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSubTypeMockMvc;

    private SubType subType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubTypeResource subTypeResource = new SubTypeResource(subTypeService);
        this.restSubTypeMockMvc = MockMvcBuilders.standaloneSetup(subTypeResource)
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
    public static SubType createEntity(EntityManager em) {
        SubType subType = new SubType()
            .subTypeNumber(DEFAULT_SUB_TYPE_NUMBER)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return subType;
    }

    @Before
    public void initTest() {
        subType = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubType() throws Exception {
        int databaseSizeBeforeCreate = subTypeRepository.findAll().size();

        // Create the SubType
        SubTypeDTO subTypeDTO = subTypeMapper.toDto(subType);
        restSubTypeMockMvc.perform(post("/api/sub-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the SubType in the database
        List<SubType> subTypeList = subTypeRepository.findAll();
        assertThat(subTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SubType testSubType = subTypeList.get(subTypeList.size() - 1);
        assertThat(testSubType.getSubTypeNumber()).isEqualTo(DEFAULT_SUB_TYPE_NUMBER);
        assertThat(testSubType.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSubType.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createSubTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subTypeRepository.findAll().size();

        // Create the SubType with an existing ID
        subType.setId(1L);
        SubTypeDTO subTypeDTO = subTypeMapper.toDto(subType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubTypeMockMvc.perform(post("/api/sub-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubType in the database
        List<SubType> subTypeList = subTypeRepository.findAll();
        assertThat(subTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSubTypeNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = subTypeRepository.findAll().size();
        // set the field null
        subType.setSubTypeNumber(null);

        // Create the SubType, which fails.
        SubTypeDTO subTypeDTO = subTypeMapper.toDto(subType);

        restSubTypeMockMvc.perform(post("/api/sub-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SubType> subTypeList = subTypeRepository.findAll();
        assertThat(subTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = subTypeRepository.findAll().size();
        // set the field null
        subType.setCreatedDate(null);

        // Create the SubType, which fails.
        SubTypeDTO subTypeDTO = subTypeMapper.toDto(subType);

        restSubTypeMockMvc.perform(post("/api/sub-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SubType> subTypeList = subTypeRepository.findAll();
        assertThat(subTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubTypes() throws Exception {
        // Initialize the database
        subTypeRepository.saveAndFlush(subType);

        // Get all the subTypeList
        restSubTypeMockMvc.perform(get("/api/sub-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subType.getId().intValue())))
            .andExpect(jsonPath("$.[*].subTypeNumber").value(hasItem(DEFAULT_SUB_TYPE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    public void getSubType() throws Exception {
        // Initialize the database
        subTypeRepository.saveAndFlush(subType);

        // Get the subType
        restSubTypeMockMvc.perform(get("/api/sub-types/{id}", subType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subType.getId().intValue()))
            .andExpect(jsonPath("$.subTypeNumber").value(DEFAULT_SUB_TYPE_NUMBER.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingSubType() throws Exception {
        // Get the subType
        restSubTypeMockMvc.perform(get("/api/sub-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubType() throws Exception {
        // Initialize the database
        subTypeRepository.saveAndFlush(subType);
        int databaseSizeBeforeUpdate = subTypeRepository.findAll().size();

        // Update the subType
        SubType updatedSubType = subTypeRepository.findOne(subType.getId());
        // Disconnect from session so that the updates on updatedSubType are not directly saved in db
        em.detach(updatedSubType);
        updatedSubType
            .subTypeNumber(UPDATED_SUB_TYPE_NUMBER)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        SubTypeDTO subTypeDTO = subTypeMapper.toDto(updatedSubType);

        restSubTypeMockMvc.perform(put("/api/sub-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTypeDTO)))
            .andExpect(status().isOk());

        // Validate the SubType in the database
        List<SubType> subTypeList = subTypeRepository.findAll();
        assertThat(subTypeList).hasSize(databaseSizeBeforeUpdate);
        SubType testSubType = subTypeList.get(subTypeList.size() - 1);
        assertThat(testSubType.getSubTypeNumber()).isEqualTo(UPDATED_SUB_TYPE_NUMBER);
        assertThat(testSubType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSubType.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingSubType() throws Exception {
        int databaseSizeBeforeUpdate = subTypeRepository.findAll().size();

        // Create the SubType
        SubTypeDTO subTypeDTO = subTypeMapper.toDto(subType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSubTypeMockMvc.perform(put("/api/sub-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the SubType in the database
        List<SubType> subTypeList = subTypeRepository.findAll();
        assertThat(subTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSubType() throws Exception {
        // Initialize the database
        subTypeRepository.saveAndFlush(subType);
        int databaseSizeBeforeDelete = subTypeRepository.findAll().size();

        // Get the subType
        restSubTypeMockMvc.perform(delete("/api/sub-types/{id}", subType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SubType> subTypeList = subTypeRepository.findAll();
        assertThat(subTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubType.class);
        SubType subType1 = new SubType();
        subType1.setId(1L);
        SubType subType2 = new SubType();
        subType2.setId(subType1.getId());
        assertThat(subType1).isEqualTo(subType2);
        subType2.setId(2L);
        assertThat(subType1).isNotEqualTo(subType2);
        subType1.setId(null);
        assertThat(subType1).isNotEqualTo(subType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubTypeDTO.class);
        SubTypeDTO subTypeDTO1 = new SubTypeDTO();
        subTypeDTO1.setId(1L);
        SubTypeDTO subTypeDTO2 = new SubTypeDTO();
        assertThat(subTypeDTO1).isNotEqualTo(subTypeDTO2);
        subTypeDTO2.setId(subTypeDTO1.getId());
        assertThat(subTypeDTO1).isEqualTo(subTypeDTO2);
        subTypeDTO2.setId(2L);
        assertThat(subTypeDTO1).isNotEqualTo(subTypeDTO2);
        subTypeDTO1.setId(null);
        assertThat(subTypeDTO1).isNotEqualTo(subTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(subTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(subTypeMapper.fromId(null)).isNull();
    }
}
