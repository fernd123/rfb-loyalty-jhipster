package com.rfb.web.rest;

import com.rfb.RfbloyaltyApp;
import com.rfb.domain.TrainingExercise;
import com.rfb.repository.TrainingExerciseRepository;
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
 * Integration tests for the {@Link TrainingExerciseResource} REST controller.
 */
@SpringBootTest(classes = RfbloyaltyApp.class)
public class TrainingExerciseResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_TRAINING_NUMBER = 1;
    private static final Integer UPDATED_TRAINING_NUMBER = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TrainingExerciseRepository trainingExerciseRepository;

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

    private MockMvc restTrainingExerciseMockMvc;

    private TrainingExercise trainingExercise;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrainingExerciseResource trainingExerciseResource = new TrainingExerciseResource(trainingExerciseRepository);
        this.restTrainingExerciseMockMvc = MockMvcBuilders.standaloneSetup(trainingExerciseResource)
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
    public static TrainingExercise createEntity(EntityManager em) {
        TrainingExercise trainingExercise = new TrainingExercise()
            .creationDate(DEFAULT_CREATION_DATE)
            .trainingNumber(DEFAULT_TRAINING_NUMBER)
            .description(DEFAULT_DESCRIPTION);
        return trainingExercise;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingExercise createUpdatedEntity(EntityManager em) {
        TrainingExercise trainingExercise = new TrainingExercise()
            .creationDate(UPDATED_CREATION_DATE)
            .trainingNumber(UPDATED_TRAINING_NUMBER)
            .description(UPDATED_DESCRIPTION);
        return trainingExercise;
    }

    @BeforeEach
    public void initTest() {
        trainingExercise = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrainingExercise() throws Exception {
        int databaseSizeBeforeCreate = trainingExerciseRepository.findAll().size();

        // Create the TrainingExercise
        restTrainingExerciseMockMvc.perform(post("/api/training-exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trainingExercise)))
            .andExpect(status().isCreated());

        // Validate the TrainingExercise in the database
        List<TrainingExercise> trainingExerciseList = trainingExerciseRepository.findAll();
        assertThat(trainingExerciseList).hasSize(databaseSizeBeforeCreate + 1);
        TrainingExercise testTrainingExercise = trainingExerciseList.get(trainingExerciseList.size() - 1);
        assertThat(testTrainingExercise.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testTrainingExercise.getTrainingNumber()).isEqualTo(DEFAULT_TRAINING_NUMBER);
        assertThat(testTrainingExercise.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTrainingExerciseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trainingExerciseRepository.findAll().size();

        // Create the TrainingExercise with an existing ID
        trainingExercise.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingExerciseMockMvc.perform(post("/api/training-exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trainingExercise)))
            .andExpect(status().isBadRequest());

        // Validate the TrainingExercise in the database
        List<TrainingExercise> trainingExerciseList = trainingExerciseRepository.findAll();
        assertThat(trainingExerciseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTrainingNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = trainingExerciseRepository.findAll().size();
        // set the field null
        trainingExercise.setTrainingNumber(null);

        // Create the TrainingExercise, which fails.

        restTrainingExerciseMockMvc.perform(post("/api/training-exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trainingExercise)))
            .andExpect(status().isBadRequest());

        List<TrainingExercise> trainingExerciseList = trainingExerciseRepository.findAll();
        assertThat(trainingExerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrainingExercises() throws Exception {
        // Initialize the database
        trainingExerciseRepository.saveAndFlush(trainingExercise);

        // Get all the trainingExerciseList
        restTrainingExerciseMockMvc.perform(get("/api/training-exercises?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingExercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].trainingNumber").value(hasItem(DEFAULT_TRAINING_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getTrainingExercise() throws Exception {
        // Initialize the database
        trainingExerciseRepository.saveAndFlush(trainingExercise);

        // Get the trainingExercise
        restTrainingExerciseMockMvc.perform(get("/api/training-exercises/{id}", trainingExercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trainingExercise.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.trainingNumber").value(DEFAULT_TRAINING_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTrainingExercise() throws Exception {
        // Get the trainingExercise
        restTrainingExerciseMockMvc.perform(get("/api/training-exercises/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrainingExercise() throws Exception {
        // Initialize the database
        trainingExerciseRepository.saveAndFlush(trainingExercise);

        int databaseSizeBeforeUpdate = trainingExerciseRepository.findAll().size();

        // Update the trainingExercise
        TrainingExercise updatedTrainingExercise = trainingExerciseRepository.findById(trainingExercise.getId()).get();
        // Disconnect from session so that the updates on updatedTrainingExercise are not directly saved in db
        em.detach(updatedTrainingExercise);
        updatedTrainingExercise
            .creationDate(UPDATED_CREATION_DATE)
            .trainingNumber(UPDATED_TRAINING_NUMBER)
            .description(UPDATED_DESCRIPTION);

        restTrainingExerciseMockMvc.perform(put("/api/training-exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrainingExercise)))
            .andExpect(status().isOk());

        // Validate the TrainingExercise in the database
        List<TrainingExercise> trainingExerciseList = trainingExerciseRepository.findAll();
        assertThat(trainingExerciseList).hasSize(databaseSizeBeforeUpdate);
        TrainingExercise testTrainingExercise = trainingExerciseList.get(trainingExerciseList.size() - 1);
        assertThat(testTrainingExercise.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testTrainingExercise.getTrainingNumber()).isEqualTo(UPDATED_TRAINING_NUMBER);
        assertThat(testTrainingExercise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingTrainingExercise() throws Exception {
        int databaseSizeBeforeUpdate = trainingExerciseRepository.findAll().size();

        // Create the TrainingExercise

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingExerciseMockMvc.perform(put("/api/training-exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trainingExercise)))
            .andExpect(status().isBadRequest());

        // Validate the TrainingExercise in the database
        List<TrainingExercise> trainingExerciseList = trainingExerciseRepository.findAll();
        assertThat(trainingExerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTrainingExercise() throws Exception {
        // Initialize the database
        trainingExerciseRepository.saveAndFlush(trainingExercise);

        int databaseSizeBeforeDelete = trainingExerciseRepository.findAll().size();

        // Delete the trainingExercise
        restTrainingExerciseMockMvc.perform(delete("/api/training-exercises/{id}", trainingExercise.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<TrainingExercise> trainingExerciseList = trainingExerciseRepository.findAll();
        assertThat(trainingExerciseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingExercise.class);
        TrainingExercise trainingExercise1 = new TrainingExercise();
        trainingExercise1.setId(1L);
        TrainingExercise trainingExercise2 = new TrainingExercise();
        trainingExercise2.setId(trainingExercise1.getId());
        assertThat(trainingExercise1).isEqualTo(trainingExercise2);
        trainingExercise2.setId(2L);
        assertThat(trainingExercise1).isNotEqualTo(trainingExercise2);
        trainingExercise1.setId(null);
        assertThat(trainingExercise1).isNotEqualTo(trainingExercise2);
    }
}
