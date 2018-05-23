package com.keyfalcon.budget.web.rest;

import com.keyfalcon.budget.BudgetApp;

import com.keyfalcon.budget.domain.TCP;
import com.keyfalcon.budget.repository.TCPRepository;
import com.keyfalcon.budget.service.TCPService;
import com.keyfalcon.budget.service.dto.TCPDTO;
import com.keyfalcon.budget.service.mapper.TCPMapper;
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
 * Test class for the TCPResource REST controller.
 *
 * @see TCPResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class TCPResourceIntTest {

    private static final String DEFAULT_PARA_NO_TCP = "AAAAAAAAAA";
    private static final String UPDATED_PARA_NO_TCP = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private TCPRepository tCPRepository;

    @Autowired
    private TCPMapper tCPMapper;

    @Autowired
    private TCPService tCPService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTCPMockMvc;

    private TCP tCP;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TCPResource tCPResource = new TCPResource(tCPService);
        this.restTCPMockMvc = MockMvcBuilders.standaloneSetup(tCPResource)
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
    public static TCP createEntity(EntityManager em) {
        TCP tCP = new TCP()
            .paraNoTCP(DEFAULT_PARA_NO_TCP)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return tCP;
    }

    @Before
    public void initTest() {
        tCP = createEntity(em);
    }

    @Test
    @Transactional
    public void createTCP() throws Exception {
        int databaseSizeBeforeCreate = tCPRepository.findAll().size();

        // Create the TCP
        TCPDTO tCPDTO = tCPMapper.toDto(tCP);
        restTCPMockMvc.perform(post("/api/tcps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tCPDTO)))
            .andExpect(status().isCreated());

        // Validate the TCP in the database
        List<TCP> tCPList = tCPRepository.findAll();
        assertThat(tCPList).hasSize(databaseSizeBeforeCreate + 1);
        TCP testTCP = tCPList.get(tCPList.size() - 1);
        assertThat(testTCP.getParaNoTCP()).isEqualTo(DEFAULT_PARA_NO_TCP);
        assertThat(testTCP.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTCP.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createTCPWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tCPRepository.findAll().size();

        // Create the TCP with an existing ID
        tCP.setId(1L);
        TCPDTO tCPDTO = tCPMapper.toDto(tCP);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTCPMockMvc.perform(post("/api/tcps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tCPDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TCP in the database
        List<TCP> tCPList = tCPRepository.findAll();
        assertThat(tCPList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkParaNoTCPIsRequired() throws Exception {
        int databaseSizeBeforeTest = tCPRepository.findAll().size();
        // set the field null
        tCP.setParaNoTCP(null);

        // Create the TCP, which fails.
        TCPDTO tCPDTO = tCPMapper.toDto(tCP);

        restTCPMockMvc.perform(post("/api/tcps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tCPDTO)))
            .andExpect(status().isBadRequest());

        List<TCP> tCPList = tCPRepository.findAll();
        assertThat(tCPList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tCPRepository.findAll().size();
        // set the field null
        tCP.setCreatedDate(null);

        // Create the TCP, which fails.
        TCPDTO tCPDTO = tCPMapper.toDto(tCP);

        restTCPMockMvc.perform(post("/api/tcps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tCPDTO)))
            .andExpect(status().isBadRequest());

        List<TCP> tCPList = tCPRepository.findAll();
        assertThat(tCPList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTCPS() throws Exception {
        // Initialize the database
        tCPRepository.saveAndFlush(tCP);

        // Get all the tCPList
        restTCPMockMvc.perform(get("/api/tcps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tCP.getId().intValue())))
            .andExpect(jsonPath("$.[*].paraNoTCP").value(hasItem(DEFAULT_PARA_NO_TCP.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    public void getTCP() throws Exception {
        // Initialize the database
        tCPRepository.saveAndFlush(tCP);

        // Get the tCP
        restTCPMockMvc.perform(get("/api/tcps/{id}", tCP.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tCP.getId().intValue()))
            .andExpect(jsonPath("$.paraNoTCP").value(DEFAULT_PARA_NO_TCP.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingTCP() throws Exception {
        // Get the tCP
        restTCPMockMvc.perform(get("/api/tcps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTCP() throws Exception {
        // Initialize the database
        tCPRepository.saveAndFlush(tCP);
        int databaseSizeBeforeUpdate = tCPRepository.findAll().size();

        // Update the tCP
        TCP updatedTCP = tCPRepository.findOne(tCP.getId());
        // Disconnect from session so that the updates on updatedTCP are not directly saved in db
        em.detach(updatedTCP);
        updatedTCP
            .paraNoTCP(UPDATED_PARA_NO_TCP)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        TCPDTO tCPDTO = tCPMapper.toDto(updatedTCP);

        restTCPMockMvc.perform(put("/api/tcps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tCPDTO)))
            .andExpect(status().isOk());

        // Validate the TCP in the database
        List<TCP> tCPList = tCPRepository.findAll();
        assertThat(tCPList).hasSize(databaseSizeBeforeUpdate);
        TCP testTCP = tCPList.get(tCPList.size() - 1);
        assertThat(testTCP.getParaNoTCP()).isEqualTo(UPDATED_PARA_NO_TCP);
        assertThat(testTCP.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTCP.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTCP() throws Exception {
        int databaseSizeBeforeUpdate = tCPRepository.findAll().size();

        // Create the TCP
        TCPDTO tCPDTO = tCPMapper.toDto(tCP);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTCPMockMvc.perform(put("/api/tcps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tCPDTO)))
            .andExpect(status().isCreated());

        // Validate the TCP in the database
        List<TCP> tCPList = tCPRepository.findAll();
        assertThat(tCPList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTCP() throws Exception {
        // Initialize the database
        tCPRepository.saveAndFlush(tCP);
        int databaseSizeBeforeDelete = tCPRepository.findAll().size();

        // Get the tCP
        restTCPMockMvc.perform(delete("/api/tcps/{id}", tCP.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TCP> tCPList = tCPRepository.findAll();
        assertThat(tCPList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TCP.class);
        TCP tCP1 = new TCP();
        tCP1.setId(1L);
        TCP tCP2 = new TCP();
        tCP2.setId(tCP1.getId());
        assertThat(tCP1).isEqualTo(tCP2);
        tCP2.setId(2L);
        assertThat(tCP1).isNotEqualTo(tCP2);
        tCP1.setId(null);
        assertThat(tCP1).isNotEqualTo(tCP2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TCPDTO.class);
        TCPDTO tCPDTO1 = new TCPDTO();
        tCPDTO1.setId(1L);
        TCPDTO tCPDTO2 = new TCPDTO();
        assertThat(tCPDTO1).isNotEqualTo(tCPDTO2);
        tCPDTO2.setId(tCPDTO1.getId());
        assertThat(tCPDTO1).isEqualTo(tCPDTO2);
        tCPDTO2.setId(2L);
        assertThat(tCPDTO1).isNotEqualTo(tCPDTO2);
        tCPDTO1.setId(null);
        assertThat(tCPDTO1).isNotEqualTo(tCPDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tCPMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tCPMapper.fromId(null)).isNull();
    }
}
