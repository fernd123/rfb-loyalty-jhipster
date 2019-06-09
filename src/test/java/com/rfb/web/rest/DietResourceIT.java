package com.rfb.web.rest;

import com.rfb.RfbloyaltyApp;
import com.rfb.domain.Diet;
import com.rfb.repository.DietRepository;
import com.rfb.web.rest.errors.ExceptionTranslator;

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
        final DietResource dietResource = new DietResource(dietRepository);
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
    public void getNonExistingDiet() throws Exception {
        // Get the diet
        restDietMockMvc.perform(get("/api/diets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiet() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

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
        dietRepository.saveAndFlush(diet);

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
