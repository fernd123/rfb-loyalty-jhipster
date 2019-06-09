package com.rfb.web.rest;

import com.rfb.RfbloyaltyApp;
import com.rfb.domain.CustomerDate;
import com.rfb.repository.CustomerDateRepository;
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
 * Integration tests for the {@Link CustomerDateResource} REST controller.
 */
@SpringBootTest(classes = RfbloyaltyApp.class)
public class CustomerDateResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OBSERVATIONS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private CustomerDateRepository customerDateRepository;

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

    private MockMvc restCustomerDateMockMvc;

    private CustomerDate customerDate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerDateResource customerDateResource = new CustomerDateResource(customerDateRepository);
        this.restCustomerDateMockMvc = MockMvcBuilders.standaloneSetup(customerDateResource)
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
    public static CustomerDate createEntity(EntityManager em) {
        CustomerDate customerDate = new CustomerDate()
            .creationDate(DEFAULT_CREATION_DATE)
            .observations(DEFAULT_OBSERVATIONS)
            .isActive(DEFAULT_IS_ACTIVE);
        return customerDate;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerDate createUpdatedEntity(EntityManager em) {
        CustomerDate customerDate = new CustomerDate()
            .creationDate(UPDATED_CREATION_DATE)
            .observations(UPDATED_OBSERVATIONS)
            .isActive(UPDATED_IS_ACTIVE);
        return customerDate;
    }

    @BeforeEach
    public void initTest() {
        customerDate = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerDate() throws Exception {
        int databaseSizeBeforeCreate = customerDateRepository.findAll().size();

        // Create the CustomerDate
        restCustomerDateMockMvc.perform(post("/api/customer-dates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDate)))
            .andExpect(status().isCreated());

        // Validate the CustomerDate in the database
        List<CustomerDate> customerDateList = customerDateRepository.findAll();
        assertThat(customerDateList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerDate testCustomerDate = customerDateList.get(customerDateList.size() - 1);
        assertThat(testCustomerDate.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCustomerDate.getObservations()).isEqualTo(DEFAULT_OBSERVATIONS);
        assertThat(testCustomerDate.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createCustomerDateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerDateRepository.findAll().size();

        // Create the CustomerDate with an existing ID
        customerDate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerDateMockMvc.perform(post("/api/customer-dates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDate)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerDate in the database
        List<CustomerDate> customerDateList = customerDateRepository.findAll();
        assertThat(customerDateList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCustomerDates() throws Exception {
        // Initialize the database
        customerDateRepository.saveAndFlush(customerDate);

        // Get all the customerDateList
        restCustomerDateMockMvc.perform(get("/api/customer-dates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerDate.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCustomerDate() throws Exception {
        // Initialize the database
        customerDateRepository.saveAndFlush(customerDate);

        // Get the customerDate
        restCustomerDateMockMvc.perform(get("/api/customer-dates/{id}", customerDate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerDate.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.observations").value(DEFAULT_OBSERVATIONS.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerDate() throws Exception {
        // Get the customerDate
        restCustomerDateMockMvc.perform(get("/api/customer-dates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerDate() throws Exception {
        // Initialize the database
        customerDateRepository.saveAndFlush(customerDate);

        int databaseSizeBeforeUpdate = customerDateRepository.findAll().size();

        // Update the customerDate
        CustomerDate updatedCustomerDate = customerDateRepository.findById(customerDate.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerDate are not directly saved in db
        em.detach(updatedCustomerDate);
        updatedCustomerDate
            .creationDate(UPDATED_CREATION_DATE)
            .observations(UPDATED_OBSERVATIONS)
            .isActive(UPDATED_IS_ACTIVE);

        restCustomerDateMockMvc.perform(put("/api/customer-dates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomerDate)))
            .andExpect(status().isOk());

        // Validate the CustomerDate in the database
        List<CustomerDate> customerDateList = customerDateRepository.findAll();
        assertThat(customerDateList).hasSize(databaseSizeBeforeUpdate);
        CustomerDate testCustomerDate = customerDateList.get(customerDateList.size() - 1);
        assertThat(testCustomerDate.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCustomerDate.getObservations()).isEqualTo(UPDATED_OBSERVATIONS);
        assertThat(testCustomerDate.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerDate() throws Exception {
        int databaseSizeBeforeUpdate = customerDateRepository.findAll().size();

        // Create the CustomerDate

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerDateMockMvc.perform(put("/api/customer-dates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDate)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerDate in the database
        List<CustomerDate> customerDateList = customerDateRepository.findAll();
        assertThat(customerDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCustomerDate() throws Exception {
        // Initialize the database
        customerDateRepository.saveAndFlush(customerDate);

        int databaseSizeBeforeDelete = customerDateRepository.findAll().size();

        // Delete the customerDate
        restCustomerDateMockMvc.perform(delete("/api/customer-dates/{id}", customerDate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<CustomerDate> customerDateList = customerDateRepository.findAll();
        assertThat(customerDateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerDate.class);
        CustomerDate customerDate1 = new CustomerDate();
        customerDate1.setId(1L);
        CustomerDate customerDate2 = new CustomerDate();
        customerDate2.setId(customerDate1.getId());
        assertThat(customerDate1).isEqualTo(customerDate2);
        customerDate2.setId(2L);
        assertThat(customerDate1).isNotEqualTo(customerDate2);
        customerDate1.setId(null);
        assertThat(customerDate1).isNotEqualTo(customerDate2);
    }
}
