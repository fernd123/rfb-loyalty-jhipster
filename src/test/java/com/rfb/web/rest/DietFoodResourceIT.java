package com.rfb.web.rest;

import com.rfb.RfbloyaltyApp;
import com.rfb.domain.DietFood;
import com.rfb.repository.DietFoodRepository;
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
 * Integration tests for the {@Link DietFoodResource} REST controller.
 */
@SpringBootTest(classes = RfbloyaltyApp.class)
public class DietFoodResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DietFoodRepository dietFoodRepository;

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

    private MockMvc restDietFoodMockMvc;

    private DietFood dietFood;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DietFoodResource dietFoodResource = new DietFoodResource(dietFoodRepository);
        this.restDietFoodMockMvc = MockMvcBuilders.standaloneSetup(dietFoodResource)
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
    public static DietFood createEntity(EntityManager em) {
        DietFood dietFood = new DietFood()
            .creationDate(DEFAULT_CREATION_DATE)
            .description(DEFAULT_DESCRIPTION);
        return dietFood;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DietFood createUpdatedEntity(EntityManager em) {
        DietFood dietFood = new DietFood()
            .creationDate(UPDATED_CREATION_DATE)
            .description(UPDATED_DESCRIPTION);
        return dietFood;
    }

    @BeforeEach
    public void initTest() {
        dietFood = createEntity(em);
    }

    @Test
    @Transactional
    public void createDietFood() throws Exception {
        int databaseSizeBeforeCreate = dietFoodRepository.findAll().size();

        // Create the DietFood
        restDietFoodMockMvc.perform(post("/api/diet-foods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dietFood)))
            .andExpect(status().isCreated());

        // Validate the DietFood in the database
        List<DietFood> dietFoodList = dietFoodRepository.findAll();
        assertThat(dietFoodList).hasSize(databaseSizeBeforeCreate + 1);
        DietFood testDietFood = dietFoodList.get(dietFoodList.size() - 1);
        assertThat(testDietFood.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testDietFood.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDietFoodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dietFoodRepository.findAll().size();

        // Create the DietFood with an existing ID
        dietFood.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDietFoodMockMvc.perform(post("/api/diet-foods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dietFood)))
            .andExpect(status().isBadRequest());

        // Validate the DietFood in the database
        List<DietFood> dietFoodList = dietFoodRepository.findAll();
        assertThat(dietFoodList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = dietFoodRepository.findAll().size();
        // set the field null
        dietFood.setDescription(null);

        // Create the DietFood, which fails.

        restDietFoodMockMvc.perform(post("/api/diet-foods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dietFood)))
            .andExpect(status().isBadRequest());

        List<DietFood> dietFoodList = dietFoodRepository.findAll();
        assertThat(dietFoodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDietFoods() throws Exception {
        // Initialize the database
        dietFoodRepository.saveAndFlush(dietFood);

        // Get all the dietFoodList
        restDietFoodMockMvc.perform(get("/api/diet-foods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dietFood.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getDietFood() throws Exception {
        // Initialize the database
        dietFoodRepository.saveAndFlush(dietFood);

        // Get the dietFood
        restDietFoodMockMvc.perform(get("/api/diet-foods/{id}", dietFood.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dietFood.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDietFood() throws Exception {
        // Get the dietFood
        restDietFoodMockMvc.perform(get("/api/diet-foods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDietFood() throws Exception {
        // Initialize the database
        dietFoodRepository.saveAndFlush(dietFood);

        int databaseSizeBeforeUpdate = dietFoodRepository.findAll().size();

        // Update the dietFood
        DietFood updatedDietFood = dietFoodRepository.findById(dietFood.getId()).get();
        // Disconnect from session so that the updates on updatedDietFood are not directly saved in db
        em.detach(updatedDietFood);
        updatedDietFood
            .creationDate(UPDATED_CREATION_DATE)
            .description(UPDATED_DESCRIPTION);

        restDietFoodMockMvc.perform(put("/api/diet-foods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDietFood)))
            .andExpect(status().isOk());

        // Validate the DietFood in the database
        List<DietFood> dietFoodList = dietFoodRepository.findAll();
        assertThat(dietFoodList).hasSize(databaseSizeBeforeUpdate);
        DietFood testDietFood = dietFoodList.get(dietFoodList.size() - 1);
        assertThat(testDietFood.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testDietFood.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDietFood() throws Exception {
        int databaseSizeBeforeUpdate = dietFoodRepository.findAll().size();

        // Create the DietFood

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDietFoodMockMvc.perform(put("/api/diet-foods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dietFood)))
            .andExpect(status().isBadRequest());

        // Validate the DietFood in the database
        List<DietFood> dietFoodList = dietFoodRepository.findAll();
        assertThat(dietFoodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDietFood() throws Exception {
        // Initialize the database
        dietFoodRepository.saveAndFlush(dietFood);

        int databaseSizeBeforeDelete = dietFoodRepository.findAll().size();

        // Delete the dietFood
        restDietFoodMockMvc.perform(delete("/api/diet-foods/{id}", dietFood.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<DietFood> dietFoodList = dietFoodRepository.findAll();
        assertThat(dietFoodList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DietFood.class);
        DietFood dietFood1 = new DietFood();
        dietFood1.setId(1L);
        DietFood dietFood2 = new DietFood();
        dietFood2.setId(dietFood1.getId());
        assertThat(dietFood1).isEqualTo(dietFood2);
        dietFood2.setId(2L);
        assertThat(dietFood1).isNotEqualTo(dietFood2);
        dietFood1.setId(null);
        assertThat(dietFood1).isNotEqualTo(dietFood2);
    }
}
