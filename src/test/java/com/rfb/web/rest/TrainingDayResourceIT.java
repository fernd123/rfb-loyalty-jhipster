package com.rfb.web.rest;

import com.rfb.RfbloyaltyApp;
import com.rfb.domain.TrainingDay;
import com.rfb.repository.TrainingDayRepository;
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
 * Integration tests for the {@Link TrainingDayResource} REST controller.
 */
@SpringBootTest(classes = RfbloyaltyApp.class)
public class TrainingDayResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TrainingDayRepository trainingDayRepository;

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

    private MockMvc restTrainingDayMockMvc;

    private TrainingDay trainingDay;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrainingDayResource trainingDayResource = new TrainingDayResource(trainingDayRepository);
        this.restTrainingDayMockMvc = MockMvcBuilders.standaloneSetup(trainingDayResource)
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
    public static TrainingDay createEntity(EntityManager em) {
        TrainingDay trainingDay = new TrainingDay()
            .creationDate(DEFAULT_CREATION_DATE)
            .name(DEFAULT_NAME);
        return trainingDay;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingDay createUpdatedEntity(EntityManager em) {
        TrainingDay trainingDay = new TrainingDay()
            .creationDate(UPDATED_CREATION_DATE)
            .name(UPDATED_NAME);
        return trainingDay;
    }

    @BeforeEach
    public void initTest() {
        trainingDay = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrainingDay() throws Exception {
        int databaseSizeBeforeCreate = trainingDayRepository.findAll().size();

        // Create the TrainingDay
        restTrainingDayMockMvc.perform(post("/api/training-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trainingDay)))
            .andExpect(status().isCreated());

        // Validate the TrainingDay in the database
        List<TrainingDay> trainingDayList = trainingDayRepository.findAll();
        assertThat(trainingDayList).hasSize(databaseSizeBeforeCreate + 1);
        TrainingDay testTrainingDay = trainingDayList.get(trainingDayList.size() - 1);
        assertThat(testTrainingDay.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testTrainingDay.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createTrainingDayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trainingDayRepository.findAll().size();

        // Create the TrainingDay with an existing ID
        trainingDay.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingDayMockMvc.perform(post("/api/training-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trainingDay)))
            .andExpect(status().isBadRequest());

        // Validate the TrainingDay in the database
        List<TrainingDay> trainingDayList = trainingDayRepository.findAll();
        assertThat(trainingDayList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trainingDayRepository.findAll().size();
        // set the field null
        trainingDay.setName(null);

        // Create the TrainingDay, which fails.

        restTrainingDayMockMvc.perform(post("/api/training-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trainingDay)))
            .andExpect(status().isBadRequest());

        List<TrainingDay> trainingDayList = trainingDayRepository.findAll();
        assertThat(trainingDayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrainingDays() throws Exception {
        // Initialize the database
        trainingDayRepository.saveAndFlush(trainingDay);

        // Get all the trainingDayList
        restTrainingDayMockMvc.perform(get("/api/training-days?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getTrainingDay() throws Exception {
        // Initialize the database
        trainingDayRepository.saveAndFlush(trainingDay);

        // Get the trainingDay
        restTrainingDayMockMvc.perform(get("/api/training-days/{id}", trainingDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trainingDay.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTrainingDay() throws Exception {
        // Get the trainingDay
        restTrainingDayMockMvc.perform(get("/api/training-days/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrainingDay() throws Exception {
        // Initialize the database
        trainingDayRepository.saveAndFlush(trainingDay);

        int databaseSizeBeforeUpdate = trainingDayRepository.findAll().size();

        // Update the trainingDay
        TrainingDay updatedTrainingDay = trainingDayRepository.findById(trainingDay.getId()).get();
        // Disconnect from session so that the updates on updatedTrainingDay are not directly saved in db
        em.detach(updatedTrainingDay);
        updatedTrainingDay
            .creationDate(UPDATED_CREATION_DATE)
            .name(UPDATED_NAME);

        restTrainingDayMockMvc.perform(put("/api/training-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrainingDay)))
            .andExpect(status().isOk());

        // Validate the TrainingDay in the database
        List<TrainingDay> trainingDayList = trainingDayRepository.findAll();
        assertThat(trainingDayList).hasSize(databaseSizeBeforeUpdate);
        TrainingDay testTrainingDay = trainingDayList.get(trainingDayList.size() - 1);
        assertThat(testTrainingDay.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testTrainingDay.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTrainingDay() throws Exception {
        int databaseSizeBeforeUpdate = trainingDayRepository.findAll().size();

        // Create the TrainingDay

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingDayMockMvc.perform(put("/api/training-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trainingDay)))
            .andExpect(status().isBadRequest());

        // Validate the TrainingDay in the database
        List<TrainingDay> trainingDayList = trainingDayRepository.findAll();
        assertThat(trainingDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTrainingDay() throws Exception {
        // Initialize the database
        trainingDayRepository.saveAndFlush(trainingDay);

        int databaseSizeBeforeDelete = trainingDayRepository.findAll().size();

        // Delete the trainingDay
        restTrainingDayMockMvc.perform(delete("/api/training-days/{id}", trainingDay.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<TrainingDay> trainingDayList = trainingDayRepository.findAll();
        assertThat(trainingDayList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingDay.class);
        TrainingDay trainingDay1 = new TrainingDay();
        trainingDay1.setId(1L);
        TrainingDay trainingDay2 = new TrainingDay();
        trainingDay2.setId(trainingDay1.getId());
        assertThat(trainingDay1).isEqualTo(trainingDay2);
        trainingDay2.setId(2L);
        assertThat(trainingDay1).isNotEqualTo(trainingDay2);
        trainingDay1.setId(null);
        assertThat(trainingDay1).isNotEqualTo(trainingDay2);
    }
}
