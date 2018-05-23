package com.keyfalcon.budget.web.rest;

import com.keyfalcon.budget.BudgetApp;

import com.keyfalcon.budget.domain.UserData;
import com.keyfalcon.budget.repository.UserDataRepository;
import com.keyfalcon.budget.service.UserDataService;
import com.keyfalcon.budget.service.dto.UserDataDTO;
import com.keyfalcon.budget.service.mapper.UserDataMapper;
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
 * Test class for the UserDataResource REST controller.
 *
 * @see UserDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetApp.class)
public class UserDataResourceIntTest {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_USER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_USER_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private UserDataMapper userDataMapper;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserDataMockMvc;

    private UserData userData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserDataResource userDataResource = new UserDataResource(userDataService);
        this.restUserDataMockMvc = MockMvcBuilders.standaloneSetup(userDataResource)
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
    public static UserData createEntity(EntityManager em) {
        UserData userData = new UserData()
            .userName(DEFAULT_USER_NAME)
            .password(DEFAULT_PASSWORD)
            .userType(DEFAULT_USER_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return userData;
    }

    @Before
    public void initTest() {
        userData = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserData() throws Exception {
        int databaseSizeBeforeCreate = userDataRepository.findAll().size();

        // Create the UserData
        UserDataDTO userDataDTO = userDataMapper.toDto(userData);
        restUserDataMockMvc.perform(post("/api/user-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDataDTO)))
            .andExpect(status().isCreated());

        // Validate the UserData in the database
        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeCreate + 1);
        UserData testUserData = userDataList.get(userDataList.size() - 1);
        assertThat(testUserData.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testUserData.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUserData.getUserType()).isEqualTo(DEFAULT_USER_TYPE);
        assertThat(testUserData.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserData.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserData.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createUserDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userDataRepository.findAll().size();

        // Create the UserData with an existing ID
        userData.setId(1L);
        UserDataDTO userDataDTO = userDataMapper.toDto(userData);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDataMockMvc.perform(post("/api/user-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserData in the database
        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userDataRepository.findAll().size();
        // set the field null
        userData.setUserName(null);

        // Create the UserData, which fails.
        UserDataDTO userDataDTO = userDataMapper.toDto(userData);

        restUserDataMockMvc.perform(post("/api/user-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDataDTO)))
            .andExpect(status().isBadRequest());

        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = userDataRepository.findAll().size();
        // set the field null
        userData.setPassword(null);

        // Create the UserData, which fails.
        UserDataDTO userDataDTO = userDataMapper.toDto(userData);

        restUserDataMockMvc.perform(post("/api/user-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDataDTO)))
            .andExpect(status().isBadRequest());

        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userDataRepository.findAll().size();
        // set the field null
        userData.setUserType(null);

        // Create the UserData, which fails.
        UserDataDTO userDataDTO = userDataMapper.toDto(userData);

        restUserDataMockMvc.perform(post("/api/user-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDataDTO)))
            .andExpect(status().isBadRequest());

        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = userDataRepository.findAll().size();
        // set the field null
        userData.setCreatedBy(null);

        // Create the UserData, which fails.
        UserDataDTO userDataDTO = userDataMapper.toDto(userData);

        restUserDataMockMvc.perform(post("/api/user-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDataDTO)))
            .andExpect(status().isBadRequest());

        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userDataRepository.findAll().size();
        // set the field null
        userData.setCreatedDate(null);

        // Create the UserData, which fails.
        UserDataDTO userDataDTO = userDataMapper.toDto(userData);

        restUserDataMockMvc.perform(post("/api/user-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDataDTO)))
            .andExpect(status().isBadRequest());

        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserData() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        // Get all the userDataList
        restUserDataMockMvc.perform(get("/api/user-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userData.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    public void getUserData() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        // Get the userData
        restUserDataMockMvc.perform(get("/api/user-data/{id}", userData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userData.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.userType").value(DEFAULT_USER_TYPE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingUserData() throws Exception {
        // Get the userData
        restUserDataMockMvc.perform(get("/api/user-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserData() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);
        int databaseSizeBeforeUpdate = userDataRepository.findAll().size();

        // Update the userData
        UserData updatedUserData = userDataRepository.findOne(userData.getId());
        // Disconnect from session so that the updates on updatedUserData are not directly saved in db
        em.detach(updatedUserData);
        updatedUserData
            .userName(UPDATED_USER_NAME)
            .password(UPDATED_PASSWORD)
            .userType(UPDATED_USER_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        UserDataDTO userDataDTO = userDataMapper.toDto(updatedUserData);

        restUserDataMockMvc.perform(put("/api/user-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDataDTO)))
            .andExpect(status().isOk());

        // Validate the UserData in the database
        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeUpdate);
        UserData testUserData = userDataList.get(userDataList.size() - 1);
        assertThat(testUserData.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testUserData.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUserData.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testUserData.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserData.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserData.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserData() throws Exception {
        int databaseSizeBeforeUpdate = userDataRepository.findAll().size();

        // Create the UserData
        UserDataDTO userDataDTO = userDataMapper.toDto(userData);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserDataMockMvc.perform(put("/api/user-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDataDTO)))
            .andExpect(status().isCreated());

        // Validate the UserData in the database
        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserData() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);
        int databaseSizeBeforeDelete = userDataRepository.findAll().size();

        // Get the userData
        restUserDataMockMvc.perform(delete("/api/user-data/{id}", userData.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserData.class);
        UserData userData1 = new UserData();
        userData1.setId(1L);
        UserData userData2 = new UserData();
        userData2.setId(userData1.getId());
        assertThat(userData1).isEqualTo(userData2);
        userData2.setId(2L);
        assertThat(userData1).isNotEqualTo(userData2);
        userData1.setId(null);
        assertThat(userData1).isNotEqualTo(userData2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDataDTO.class);
        UserDataDTO userDataDTO1 = new UserDataDTO();
        userDataDTO1.setId(1L);
        UserDataDTO userDataDTO2 = new UserDataDTO();
        assertThat(userDataDTO1).isNotEqualTo(userDataDTO2);
        userDataDTO2.setId(userDataDTO1.getId());
        assertThat(userDataDTO1).isEqualTo(userDataDTO2);
        userDataDTO2.setId(2L);
        assertThat(userDataDTO1).isNotEqualTo(userDataDTO2);
        userDataDTO1.setId(null);
        assertThat(userDataDTO1).isNotEqualTo(userDataDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userDataMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userDataMapper.fromId(null)).isNull();
    }
}
