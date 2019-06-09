package com.rfb.web.rest;

import com.rfb.RfbloyaltyApp;
import com.rfb.domain.Measure;
import com.rfb.domain.Customer;
import com.rfb.repository.MeasureRepository;
import com.rfb.web.rest.errors.ExceptionTranslator;
import com.rfb.service.dto.MeasureCriteria;
import com.rfb.service.MeasureQueryService;

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
 * Integration tests for the {@Link MeasureResource} REST controller.
 */
@SpringBootTest(classes = RfbloyaltyApp.class)
public class MeasureResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_ARM = 1D;
    private static final Double UPDATED_ARM = 2D;

    private static final Double DEFAULT_RIB_CAGE = 1D;
    private static final Double UPDATED_RIB_CAGE = 2D;

    private static final Double DEFAULT_LEG = 1D;
    private static final Double UPDATED_LEG = 2D;

    @Autowired
    private MeasureRepository measureRepository;

    @Autowired
    private MeasureQueryService measureQueryService;

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

    private MockMvc restMeasureMockMvc;

    private Measure measure;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MeasureResource measureResource = new MeasureResource(measureRepository);
        this.restMeasureMockMvc = MockMvcBuilders.standaloneSetup(measureResource)
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
    public static Measure createEntity(EntityManager em) {
        Measure measure = new Measure()
            .creationDate(DEFAULT_CREATION_DATE)
            .arm(DEFAULT_ARM)
            .ribCage(DEFAULT_RIB_CAGE)
            .leg(DEFAULT_LEG);
        return measure;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Measure createUpdatedEntity(EntityManager em) {
        Measure measure = new Measure()
            .creationDate(UPDATED_CREATION_DATE)
            .arm(UPDATED_ARM)
            .ribCage(UPDATED_RIB_CAGE)
            .leg(UPDATED_LEG);
        return measure;
    }

    @BeforeEach
    public void initTest() {
        measure = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeasure() throws Exception {
        int databaseSizeBeforeCreate = measureRepository.findAll().size();

        // Create the Measure
        restMeasureMockMvc.perform(post("/api/measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measure)))
            .andExpect(status().isCreated());

        // Validate the Measure in the database
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeCreate + 1);
        Measure testMeasure = measureList.get(measureList.size() - 1);
        assertThat(testMeasure.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testMeasure.getArm()).isEqualTo(DEFAULT_ARM);
        assertThat(testMeasure.getRibCage()).isEqualTo(DEFAULT_RIB_CAGE);
        assertThat(testMeasure.getLeg()).isEqualTo(DEFAULT_LEG);
    }

    @Test
    @Transactional
    public void createMeasureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = measureRepository.findAll().size();

        // Create the Measure with an existing ID
        measure.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasureMockMvc.perform(post("/api/measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measure)))
            .andExpect(status().isBadRequest());

        // Validate the Measure in the database
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMeasures() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList
        restMeasureMockMvc.perform(get("/api/measures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measure.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].arm").value(hasItem(DEFAULT_ARM.doubleValue())))
            .andExpect(jsonPath("$.[*].ribCage").value(hasItem(DEFAULT_RIB_CAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].leg").value(hasItem(DEFAULT_LEG.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getMeasure() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get the measure
        restMeasureMockMvc.perform(get("/api/measures/{id}", measure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(measure.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.arm").value(DEFAULT_ARM.doubleValue()))
            .andExpect(jsonPath("$.ribCage").value(DEFAULT_RIB_CAGE.doubleValue()))
            .andExpect(jsonPath("$.leg").value(DEFAULT_LEG.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllMeasuresByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList where creationDate equals to DEFAULT_CREATION_DATE
        defaultMeasureShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the measureList where creationDate equals to UPDATED_CREATION_DATE
        defaultMeasureShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMeasuresByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultMeasureShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the measureList where creationDate equals to UPDATED_CREATION_DATE
        defaultMeasureShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllMeasuresByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList where creationDate is not null
        defaultMeasureShouldBeFound("creationDate.specified=true");

        // Get all the measureList where creationDate is null
        defaultMeasureShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllMeasuresByArmIsEqualToSomething() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList where arm equals to DEFAULT_ARM
        defaultMeasureShouldBeFound("arm.equals=" + DEFAULT_ARM);

        // Get all the measureList where arm equals to UPDATED_ARM
        defaultMeasureShouldNotBeFound("arm.equals=" + UPDATED_ARM);
    }

    @Test
    @Transactional
    public void getAllMeasuresByArmIsInShouldWork() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList where arm in DEFAULT_ARM or UPDATED_ARM
        defaultMeasureShouldBeFound("arm.in=" + DEFAULT_ARM + "," + UPDATED_ARM);

        // Get all the measureList where arm equals to UPDATED_ARM
        defaultMeasureShouldNotBeFound("arm.in=" + UPDATED_ARM);
    }

    @Test
    @Transactional
    public void getAllMeasuresByArmIsNullOrNotNull() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList where arm is not null
        defaultMeasureShouldBeFound("arm.specified=true");

        // Get all the measureList where arm is null
        defaultMeasureShouldNotBeFound("arm.specified=false");
    }

    @Test
    @Transactional
    public void getAllMeasuresByRibCageIsEqualToSomething() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList where ribCage equals to DEFAULT_RIB_CAGE
        defaultMeasureShouldBeFound("ribCage.equals=" + DEFAULT_RIB_CAGE);

        // Get all the measureList where ribCage equals to UPDATED_RIB_CAGE
        defaultMeasureShouldNotBeFound("ribCage.equals=" + UPDATED_RIB_CAGE);
    }

    @Test
    @Transactional
    public void getAllMeasuresByRibCageIsInShouldWork() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList where ribCage in DEFAULT_RIB_CAGE or UPDATED_RIB_CAGE
        defaultMeasureShouldBeFound("ribCage.in=" + DEFAULT_RIB_CAGE + "," + UPDATED_RIB_CAGE);

        // Get all the measureList where ribCage equals to UPDATED_RIB_CAGE
        defaultMeasureShouldNotBeFound("ribCage.in=" + UPDATED_RIB_CAGE);
    }

    @Test
    @Transactional
    public void getAllMeasuresByRibCageIsNullOrNotNull() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList where ribCage is not null
        defaultMeasureShouldBeFound("ribCage.specified=true");

        // Get all the measureList where ribCage is null
        defaultMeasureShouldNotBeFound("ribCage.specified=false");
    }

    @Test
    @Transactional
    public void getAllMeasuresByLegIsEqualToSomething() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList where leg equals to DEFAULT_LEG
        defaultMeasureShouldBeFound("leg.equals=" + DEFAULT_LEG);

        // Get all the measureList where leg equals to UPDATED_LEG
        defaultMeasureShouldNotBeFound("leg.equals=" + UPDATED_LEG);
    }

    @Test
    @Transactional
    public void getAllMeasuresByLegIsInShouldWork() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList where leg in DEFAULT_LEG or UPDATED_LEG
        defaultMeasureShouldBeFound("leg.in=" + DEFAULT_LEG + "," + UPDATED_LEG);

        // Get all the measureList where leg equals to UPDATED_LEG
        defaultMeasureShouldNotBeFound("leg.in=" + UPDATED_LEG);
    }

    @Test
    @Transactional
    public void getAllMeasuresByLegIsNullOrNotNull() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList where leg is not null
        defaultMeasureShouldBeFound("leg.specified=true");

        // Get all the measureList where leg is null
        defaultMeasureShouldNotBeFound("leg.specified=false");
    }

    @Test
    @Transactional
    public void getAllMeasuresByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        Customer customer = CustomerResourceIT.createEntity(em);
        em.persist(customer);
        em.flush();
        measure.setCustomer(customer);
        measureRepository.saveAndFlush(measure);
        Long customerId = customer.getId();

        // Get all the measureList where customer equals to customerId
        defaultMeasureShouldBeFound("customerId.equals=" + customerId);

        // Get all the measureList where customer equals to customerId + 1
        defaultMeasureShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMeasureShouldBeFound(String filter) throws Exception {
        restMeasureMockMvc.perform(get("/api/measures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measure.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].arm").value(hasItem(DEFAULT_ARM.doubleValue())))
            .andExpect(jsonPath("$.[*].ribCage").value(hasItem(DEFAULT_RIB_CAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].leg").value(hasItem(DEFAULT_LEG.doubleValue())));

        // Check, that the count call also returns 1
        restMeasureMockMvc.perform(get("/api/measures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMeasureShouldNotBeFound(String filter) throws Exception {
        restMeasureMockMvc.perform(get("/api/measures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMeasureMockMvc.perform(get("/api/measures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMeasure() throws Exception {
        // Get the measure
        restMeasureMockMvc.perform(get("/api/measures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeasure() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        int databaseSizeBeforeUpdate = measureRepository.findAll().size();

        // Update the measure
        Measure updatedMeasure = measureRepository.findById(measure.getId()).get();
        // Disconnect from session so that the updates on updatedMeasure are not directly saved in db
        em.detach(updatedMeasure);
        updatedMeasure
            .creationDate(UPDATED_CREATION_DATE)
            .arm(UPDATED_ARM)
            .ribCage(UPDATED_RIB_CAGE)
            .leg(UPDATED_LEG);

        restMeasureMockMvc.perform(put("/api/measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMeasure)))
            .andExpect(status().isOk());

        // Validate the Measure in the database
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeUpdate);
        Measure testMeasure = measureList.get(measureList.size() - 1);
        assertThat(testMeasure.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testMeasure.getArm()).isEqualTo(UPDATED_ARM);
        assertThat(testMeasure.getRibCage()).isEqualTo(UPDATED_RIB_CAGE);
        assertThat(testMeasure.getLeg()).isEqualTo(UPDATED_LEG);
    }

    @Test
    @Transactional
    public void updateNonExistingMeasure() throws Exception {
        int databaseSizeBeforeUpdate = measureRepository.findAll().size();

        // Create the Measure

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasureMockMvc.perform(put("/api/measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measure)))
            .andExpect(status().isBadRequest());

        // Validate the Measure in the database
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMeasure() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        int databaseSizeBeforeDelete = measureRepository.findAll().size();

        // Delete the measure
        restMeasureMockMvc.perform(delete("/api/measures/{id}", measure.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Measure.class);
        Measure measure1 = new Measure();
        measure1.setId(1L);
        Measure measure2 = new Measure();
        measure2.setId(measure1.getId());
        assertThat(measure1).isEqualTo(measure2);
        measure2.setId(2L);
        assertThat(measure1).isNotEqualTo(measure2);
        measure1.setId(null);
        assertThat(measure1).isNotEqualTo(measure2);
    }
}
