package com.rfb.web.rest;

import com.rfb.RfbloyaltyApp;
import com.rfb.domain.Diet;
import com.rfb.domain.Customer;
import com.rfb.domain.DietFood;
import com.rfb.repository.DietRepository;
import com.rfb.service.DietService;
import com.rfb.web.rest.errors.ExceptionTranslator;
import com.rfb.service.dto.DietCriteria;
import com.rfb.service.DietQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.rfb.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link DietResource} REST controller.
 */
@SpringBootTest(classes = RfbloyaltyApp.class)
public class DietResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private DietService dietService;

    @Autowired
    private DietQueryService dietQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDietMockMvc;

    private Diet diet;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DietResource dietResource = new DietResource(dietService, dietQueryService);
        this.restDietMockMvc = MockMvcBuilders.standaloneSetup(dietResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diet createEntity(EntityManager em) {
        Diet diet = new Diet()
            .creationDate(DEFAULT_CREATION_DATE)
            .name(DEFAULT_NAME);
        return diet;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diet createUpdatedEntity(EntityManager em) {
        Diet diet = new Diet()
            .creationDate(UPDATED_CREATION_DATE)
            .name(UPDATED_NAME);
        return diet;
    }

    @BeforeEach
    public void initTest() {
        diet = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiet() throws Exception {
        int databaseSizeBeforeCreate = dietRepository.findAll().size();

        // Create the Diet
        restDietMockMvc.perform(post("/api/diets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diet)))
            .andExpect(status().isCreated());

        // Validate the Diet in the database
        List<Diet> dietList = dietRepository.findAll();
        assertThat(dietList).hasSize(databaseSizeBeforeCreate + 1);
        Diet testDiet = dietList.get(dietList.size() - 1);
        assertThat(testDiet.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testDiet.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createDietWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dietRepository.findAll().size();

        // Create the Diet with an existing ID
        diet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDietMockMvc.perform(post("/api/diets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diet)))
            .andExpect(status().isBadRequest());

        // Validate the Diet in the database
        List<Diet> dietList = dietRepository.findAll();
        assertThat(dietList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dietRepository.findAll().size();
        // set the field null
        diet.setName(null);

        // Create the Diet, which fails.

        restDietMockMvc.perform(post("/api/diets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diet)))
            .andExpect(status().isBadRequest());

        List<Diet> dietList = dietRepository.findAll();
        assertThat(dietList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiets() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList
        restDietMockMvc.perform(get("/api/diets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diet.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getDiet() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get the diet
        restDietMockMvc.perform(get("/api/diets/{id}", diet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(diet.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllDietsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where creationDate equals to DEFAULT_CREATION_DATE
        defaultDietShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the dietList where creationDate equals to UPDATED_CREATION_DATE
        defaultDietShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllDietsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultDietShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the dietList where creationDate equals to UPDATED_CREATION_DATE
        defaultDietShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllDietsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where creationDate is not null
        defaultDietShouldBeFound("creationDate.specified=true");

        // Get all the dietList where creationDate is null
        defaultDietShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDietsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where name equals to DEFAULT_NAME
        defaultDietShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the dietList where name equals to UPDATED_NAME
        defaultDietShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDietsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDietShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the dietList where name equals to UPDATED_NAME
        defaultDietShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDietsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where name is not null
        defaultDietShouldBeFound("name.specified=true");

        // Get all the dietList where name is null
        defaultDietShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllDietsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        Customer customer = CustomerResourceIT.createEntity(em);
        em.persist(customer);
        em.flush();
        diet.setCustomer(customer);
        dietRepository.saveAndFlush(diet);
        Long customerId = customer.getId();

        // Get all the dietList where customer equals to customerId
        defaultDietShouldBeFound("customerId.equals=" + customerId);

        // Get all the dietList where customer equals to customerId + 1
        defaultDietShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }


    @Test
    @Transactional
    public void getAllDietsByDietFoodIsEqualToSomething() throws Exception {
        // Initialize the database
        DietFood dietFood = DietFoodResourceIT.createEntity(em);
        em.persist(dietFood);
        em.flush();
        diet.addDietFood(dietFood);
        dietRepository.saveAndFlush(diet);
        Long dietFoodId = dietFood.getId();

        // Get all the dietList where dietFood equals to dietFoodId
        defaultDietShouldBeFound("dietFoodId.equals=" + dietFoodId);

        // Get all the dietList where dietFood equals to dietFoodId + 1
        defaultDietShouldNotBeFound("dietFoodId.equals=" + (dietFoodId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDietShouldBeFound(String filter) throws Exception {
        restDietMockMvc.perform(get("/api/diets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diet.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restDietMockMvc.perform(get("/api/diets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDietShouldNotBeFound(String filter) throws Exception {
        restDietMockMvc.perform(get("/api/diets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDietMockMvc.perform(get("/api/diets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDiet() throws Exception {
        // Get the diet
        restDietMockMvc.perform(get("/api/diets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiet() throws Exception {
        // Initialize the database
        dietService.save(diet);

        int databaseSizeBeforeUpdate = dietRepository.findAll().size();

        // Update the diet
        Diet updatedDiet = dietRepository.findById(diet.getId()).get();
        // Disconnect from session so that the updates on updatedDiet are not directly saved in db
        em.detach(updatedDiet);
        updatedDiet
            .creationDate(UPDATED_CREATION_DATE)
            .name(UPDATED_NAME);

        restDietMockMvc.perform(put("/api/diets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDiet)))
            .andExpect(status().isOk());

        // Validate the Diet in the database
        List<Diet> dietList = dietRepository.findAll();
        assertThat(dietList).hasSize(databaseSizeBeforeUpdate);
        Diet testDiet = dietList.get(dietList.size() - 1);
        assertThat(testDiet.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testDiet.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingDiet() throws Exception {
        int databaseSizeBeforeUpdate = dietRepository.findAll().size();

        // Create the Diet

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDietMockMvc.perform(put("/api/diets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diet)))
            .andExpect(status().isBadRequest());

        // Validate the Diet in the database
        List<Diet> dietList = dietRepository.findAll();
        assertThat(dietList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDiet() throws Exception {
        // Initialize the database
        dietService.save(diet);

        int databaseSizeBeforeDelete = dietRepository.findAll().size();

        // Delete the diet
        restDietMockMvc.perform(delete("/api/diets/{id}", diet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Diet> dietList = dietRepository.findAll();
        assertThat(dietList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Diet.class);
        Diet diet1 = new Diet();
        diet1.setId(1L);
        Diet diet2 = new Diet();
        diet2.setId(diet1.getId());
        assertThat(diet1).isEqualTo(diet2);
        diet2.setId(2L);
        assertThat(diet1).isNotEqualTo(diet2);
        diet1.setId(null);
        assertThat(diet1).isNotEqualTo(diet2);
    }
}
