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

    private static final String DEFAULT_FOOD_1 = "AAAAAAAAAA";
    private static final String UPDATED_FOOD_1 = "BBBBBBBBBB";

    private static final String DEFAULT_FOOD_2 = "AAAAAAAAAA";
    private static final String UPDATED_FOOD_2 = "BBBBBBBBBB";

    private static final String DEFAULT_FOOD_3 = "AAAAAAAAAA";
    private static final String UPDATED_FOOD_3 = "BBBBBBBBBB";

    private static final String DEFAULT_FOOD_4 = "AAAAAAAAAA";
    private static final String UPDATED_FOOD_4 = "BBBBBBBBBB";

    private static final String DEFAULT_FOOD_5 = "AAAAAAAAAA";
    private static final String UPDATED_FOOD_5 = "BBBBBBBBBB";

    private static final String DEFAULT_FOOD_6 = "AAAAAAAAAA";
    private static final String UPDATED_FOOD_6 = "BBBBBBBBBB";

    private static final String DEFAULT_FOOD_7 = "AAAAAAAAAA";
    private static final String UPDATED_FOOD_7 = "BBBBBBBBBB";

    private static final String DEFAULT_FOOD_8 = "AAAAAAAAAA";
    private static final String UPDATED_FOOD_8 = "BBBBBBBBBB";

    private static final String DEFAULT_FOOD_9 = "AAAAAAAAAA";
    private static final String UPDATED_FOOD_9 = "BBBBBBBBBB";

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
            .name(DEFAULT_NAME)
            .food1(DEFAULT_FOOD_1)
            .food2(DEFAULT_FOOD_2)
            .food3(DEFAULT_FOOD_3)
            .food4(DEFAULT_FOOD_4)
            .food5(DEFAULT_FOOD_5)
            .food6(DEFAULT_FOOD_6)
            .food7(DEFAULT_FOOD_7)
            .food8(DEFAULT_FOOD_8)
            .food9(DEFAULT_FOOD_9);
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
            .name(UPDATED_NAME)
            .food1(UPDATED_FOOD_1)
            .food2(UPDATED_FOOD_2)
            .food3(UPDATED_FOOD_3)
            .food4(UPDATED_FOOD_4)
            .food5(UPDATED_FOOD_5)
            .food6(UPDATED_FOOD_6)
            .food7(UPDATED_FOOD_7)
            .food8(UPDATED_FOOD_8)
            .food9(UPDATED_FOOD_9);
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
        assertThat(testDiet.getFood1()).isEqualTo(DEFAULT_FOOD_1);
        assertThat(testDiet.getFood2()).isEqualTo(DEFAULT_FOOD_2);
        assertThat(testDiet.getFood3()).isEqualTo(DEFAULT_FOOD_3);
        assertThat(testDiet.getFood4()).isEqualTo(DEFAULT_FOOD_4);
        assertThat(testDiet.getFood5()).isEqualTo(DEFAULT_FOOD_5);
        assertThat(testDiet.getFood6()).isEqualTo(DEFAULT_FOOD_6);
        assertThat(testDiet.getFood7()).isEqualTo(DEFAULT_FOOD_7);
        assertThat(testDiet.getFood8()).isEqualTo(DEFAULT_FOOD_8);
        assertThat(testDiet.getFood9()).isEqualTo(DEFAULT_FOOD_9);
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
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].food1").value(hasItem(DEFAULT_FOOD_1.toString())))
            .andExpect(jsonPath("$.[*].food2").value(hasItem(DEFAULT_FOOD_2.toString())))
            .andExpect(jsonPath("$.[*].food3").value(hasItem(DEFAULT_FOOD_3.toString())))
            .andExpect(jsonPath("$.[*].food4").value(hasItem(DEFAULT_FOOD_4.toString())))
            .andExpect(jsonPath("$.[*].food5").value(hasItem(DEFAULT_FOOD_5.toString())))
            .andExpect(jsonPath("$.[*].food6").value(hasItem(DEFAULT_FOOD_6.toString())))
            .andExpect(jsonPath("$.[*].food7").value(hasItem(DEFAULT_FOOD_7.toString())))
            .andExpect(jsonPath("$.[*].food8").value(hasItem(DEFAULT_FOOD_8.toString())))
            .andExpect(jsonPath("$.[*].food9").value(hasItem(DEFAULT_FOOD_9.toString())));
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
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.food1").value(DEFAULT_FOOD_1.toString()))
            .andExpect(jsonPath("$.food2").value(DEFAULT_FOOD_2.toString()))
            .andExpect(jsonPath("$.food3").value(DEFAULT_FOOD_3.toString()))
            .andExpect(jsonPath("$.food4").value(DEFAULT_FOOD_4.toString()))
            .andExpect(jsonPath("$.food5").value(DEFAULT_FOOD_5.toString()))
            .andExpect(jsonPath("$.food6").value(DEFAULT_FOOD_6.toString()))
            .andExpect(jsonPath("$.food7").value(DEFAULT_FOOD_7.toString()))
            .andExpect(jsonPath("$.food8").value(DEFAULT_FOOD_8.toString()))
            .andExpect(jsonPath("$.food9").value(DEFAULT_FOOD_9.toString()));
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
    public void getAllDietsByFood1IsEqualToSomething() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food1 equals to DEFAULT_FOOD_1
        defaultDietShouldBeFound("food1.equals=" + DEFAULT_FOOD_1);

        // Get all the dietList where food1 equals to UPDATED_FOOD_1
        defaultDietShouldNotBeFound("food1.equals=" + UPDATED_FOOD_1);
    }

    @Test
    @Transactional
    public void getAllDietsByFood1IsInShouldWork() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food1 in DEFAULT_FOOD_1 or UPDATED_FOOD_1
        defaultDietShouldBeFound("food1.in=" + DEFAULT_FOOD_1 + "," + UPDATED_FOOD_1);

        // Get all the dietList where food1 equals to UPDATED_FOOD_1
        defaultDietShouldNotBeFound("food1.in=" + UPDATED_FOOD_1);
    }

    @Test
    @Transactional
    public void getAllDietsByFood1IsNullOrNotNull() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food1 is not null
        defaultDietShouldBeFound("food1.specified=true");

        // Get all the dietList where food1 is null
        defaultDietShouldNotBeFound("food1.specified=false");
    }

    @Test
    @Transactional
    public void getAllDietsByFood2IsEqualToSomething() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food2 equals to DEFAULT_FOOD_2
        defaultDietShouldBeFound("food2.equals=" + DEFAULT_FOOD_2);

        // Get all the dietList where food2 equals to UPDATED_FOOD_2
        defaultDietShouldNotBeFound("food2.equals=" + UPDATED_FOOD_2);
    }

    @Test
    @Transactional
    public void getAllDietsByFood2IsInShouldWork() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food2 in DEFAULT_FOOD_2 or UPDATED_FOOD_2
        defaultDietShouldBeFound("food2.in=" + DEFAULT_FOOD_2 + "," + UPDATED_FOOD_2);

        // Get all the dietList where food2 equals to UPDATED_FOOD_2
        defaultDietShouldNotBeFound("food2.in=" + UPDATED_FOOD_2);
    }

    @Test
    @Transactional
    public void getAllDietsByFood2IsNullOrNotNull() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food2 is not null
        defaultDietShouldBeFound("food2.specified=true");

        // Get all the dietList where food2 is null
        defaultDietShouldNotBeFound("food2.specified=false");
    }

    @Test
    @Transactional
    public void getAllDietsByFood3IsEqualToSomething() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food3 equals to DEFAULT_FOOD_3
        defaultDietShouldBeFound("food3.equals=" + DEFAULT_FOOD_3);

        // Get all the dietList where food3 equals to UPDATED_FOOD_3
        defaultDietShouldNotBeFound("food3.equals=" + UPDATED_FOOD_3);
    }

    @Test
    @Transactional
    public void getAllDietsByFood3IsInShouldWork() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food3 in DEFAULT_FOOD_3 or UPDATED_FOOD_3
        defaultDietShouldBeFound("food3.in=" + DEFAULT_FOOD_3 + "," + UPDATED_FOOD_3);

        // Get all the dietList where food3 equals to UPDATED_FOOD_3
        defaultDietShouldNotBeFound("food3.in=" + UPDATED_FOOD_3);
    }

    @Test
    @Transactional
    public void getAllDietsByFood3IsNullOrNotNull() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food3 is not null
        defaultDietShouldBeFound("food3.specified=true");

        // Get all the dietList where food3 is null
        defaultDietShouldNotBeFound("food3.specified=false");
    }

    @Test
    @Transactional
    public void getAllDietsByFood4IsEqualToSomething() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food4 equals to DEFAULT_FOOD_4
        defaultDietShouldBeFound("food4.equals=" + DEFAULT_FOOD_4);

        // Get all the dietList where food4 equals to UPDATED_FOOD_4
        defaultDietShouldNotBeFound("food4.equals=" + UPDATED_FOOD_4);
    }

    @Test
    @Transactional
    public void getAllDietsByFood4IsInShouldWork() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food4 in DEFAULT_FOOD_4 or UPDATED_FOOD_4
        defaultDietShouldBeFound("food4.in=" + DEFAULT_FOOD_4 + "," + UPDATED_FOOD_4);

        // Get all the dietList where food4 equals to UPDATED_FOOD_4
        defaultDietShouldNotBeFound("food4.in=" + UPDATED_FOOD_4);
    }

    @Test
    @Transactional
    public void getAllDietsByFood4IsNullOrNotNull() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food4 is not null
        defaultDietShouldBeFound("food4.specified=true");

        // Get all the dietList where food4 is null
        defaultDietShouldNotBeFound("food4.specified=false");
    }

    @Test
    @Transactional
    public void getAllDietsByFood5IsEqualToSomething() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food5 equals to DEFAULT_FOOD_5
        defaultDietShouldBeFound("food5.equals=" + DEFAULT_FOOD_5);

        // Get all the dietList where food5 equals to UPDATED_FOOD_5
        defaultDietShouldNotBeFound("food5.equals=" + UPDATED_FOOD_5);
    }

    @Test
    @Transactional
    public void getAllDietsByFood5IsInShouldWork() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food5 in DEFAULT_FOOD_5 or UPDATED_FOOD_5
        defaultDietShouldBeFound("food5.in=" + DEFAULT_FOOD_5 + "," + UPDATED_FOOD_5);

        // Get all the dietList where food5 equals to UPDATED_FOOD_5
        defaultDietShouldNotBeFound("food5.in=" + UPDATED_FOOD_5);
    }

    @Test
    @Transactional
    public void getAllDietsByFood5IsNullOrNotNull() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food5 is not null
        defaultDietShouldBeFound("food5.specified=true");

        // Get all the dietList where food5 is null
        defaultDietShouldNotBeFound("food5.specified=false");
    }

    @Test
    @Transactional
    public void getAllDietsByFood6IsEqualToSomething() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food6 equals to DEFAULT_FOOD_6
        defaultDietShouldBeFound("food6.equals=" + DEFAULT_FOOD_6);

        // Get all the dietList where food6 equals to UPDATED_FOOD_6
        defaultDietShouldNotBeFound("food6.equals=" + UPDATED_FOOD_6);
    }

    @Test
    @Transactional
    public void getAllDietsByFood6IsInShouldWork() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food6 in DEFAULT_FOOD_6 or UPDATED_FOOD_6
        defaultDietShouldBeFound("food6.in=" + DEFAULT_FOOD_6 + "," + UPDATED_FOOD_6);

        // Get all the dietList where food6 equals to UPDATED_FOOD_6
        defaultDietShouldNotBeFound("food6.in=" + UPDATED_FOOD_6);
    }

    @Test
    @Transactional
    public void getAllDietsByFood6IsNullOrNotNull() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food6 is not null
        defaultDietShouldBeFound("food6.specified=true");

        // Get all the dietList where food6 is null
        defaultDietShouldNotBeFound("food6.specified=false");
    }

    @Test
    @Transactional
    public void getAllDietsByFood7IsEqualToSomething() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food7 equals to DEFAULT_FOOD_7
        defaultDietShouldBeFound("food7.equals=" + DEFAULT_FOOD_7);

        // Get all the dietList where food7 equals to UPDATED_FOOD_7
        defaultDietShouldNotBeFound("food7.equals=" + UPDATED_FOOD_7);
    }

    @Test
    @Transactional
    public void getAllDietsByFood7IsInShouldWork() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food7 in DEFAULT_FOOD_7 or UPDATED_FOOD_7
        defaultDietShouldBeFound("food7.in=" + DEFAULT_FOOD_7 + "," + UPDATED_FOOD_7);

        // Get all the dietList where food7 equals to UPDATED_FOOD_7
        defaultDietShouldNotBeFound("food7.in=" + UPDATED_FOOD_7);
    }

    @Test
    @Transactional
    public void getAllDietsByFood7IsNullOrNotNull() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food7 is not null
        defaultDietShouldBeFound("food7.specified=true");

        // Get all the dietList where food7 is null
        defaultDietShouldNotBeFound("food7.specified=false");
    }

    @Test
    @Transactional
    public void getAllDietsByFood8IsEqualToSomething() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food8 equals to DEFAULT_FOOD_8
        defaultDietShouldBeFound("food8.equals=" + DEFAULT_FOOD_8);

        // Get all the dietList where food8 equals to UPDATED_FOOD_8
        defaultDietShouldNotBeFound("food8.equals=" + UPDATED_FOOD_8);
    }

    @Test
    @Transactional
    public void getAllDietsByFood8IsInShouldWork() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food8 in DEFAULT_FOOD_8 or UPDATED_FOOD_8
        defaultDietShouldBeFound("food8.in=" + DEFAULT_FOOD_8 + "," + UPDATED_FOOD_8);

        // Get all the dietList where food8 equals to UPDATED_FOOD_8
        defaultDietShouldNotBeFound("food8.in=" + UPDATED_FOOD_8);
    }

    @Test
    @Transactional
    public void getAllDietsByFood8IsNullOrNotNull() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food8 is not null
        defaultDietShouldBeFound("food8.specified=true");

        // Get all the dietList where food8 is null
        defaultDietShouldNotBeFound("food8.specified=false");
    }

    @Test
    @Transactional
    public void getAllDietsByFood9IsEqualToSomething() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food9 equals to DEFAULT_FOOD_9
        defaultDietShouldBeFound("food9.equals=" + DEFAULT_FOOD_9);

        // Get all the dietList where food9 equals to UPDATED_FOOD_9
        defaultDietShouldNotBeFound("food9.equals=" + UPDATED_FOOD_9);
    }

    @Test
    @Transactional
    public void getAllDietsByFood9IsInShouldWork() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food9 in DEFAULT_FOOD_9 or UPDATED_FOOD_9
        defaultDietShouldBeFound("food9.in=" + DEFAULT_FOOD_9 + "," + UPDATED_FOOD_9);

        // Get all the dietList where food9 equals to UPDATED_FOOD_9
        defaultDietShouldNotBeFound("food9.in=" + UPDATED_FOOD_9);
    }

    @Test
    @Transactional
    public void getAllDietsByFood9IsNullOrNotNull() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the dietList where food9 is not null
        defaultDietShouldBeFound("food9.specified=true");

        // Get all the dietList where food9 is null
        defaultDietShouldNotBeFound("food9.specified=false");
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
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].food1").value(hasItem(DEFAULT_FOOD_1)))
            .andExpect(jsonPath("$.[*].food2").value(hasItem(DEFAULT_FOOD_2)))
            .andExpect(jsonPath("$.[*].food3").value(hasItem(DEFAULT_FOOD_3)))
            .andExpect(jsonPath("$.[*].food4").value(hasItem(DEFAULT_FOOD_4)))
            .andExpect(jsonPath("$.[*].food5").value(hasItem(DEFAULT_FOOD_5)))
            .andExpect(jsonPath("$.[*].food6").value(hasItem(DEFAULT_FOOD_6)))
            .andExpect(jsonPath("$.[*].food7").value(hasItem(DEFAULT_FOOD_7)))
            .andExpect(jsonPath("$.[*].food8").value(hasItem(DEFAULT_FOOD_8)))
            .andExpect(jsonPath("$.[*].food9").value(hasItem(DEFAULT_FOOD_9)));

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
            .name(UPDATED_NAME)
            .food1(UPDATED_FOOD_1)
            .food2(UPDATED_FOOD_2)
            .food3(UPDATED_FOOD_3)
            .food4(UPDATED_FOOD_4)
            .food5(UPDATED_FOOD_5)
            .food6(UPDATED_FOOD_6)
            .food7(UPDATED_FOOD_7)
            .food8(UPDATED_FOOD_8)
            .food9(UPDATED_FOOD_9);

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
        assertThat(testDiet.getFood1()).isEqualTo(UPDATED_FOOD_1);
        assertThat(testDiet.getFood2()).isEqualTo(UPDATED_FOOD_2);
        assertThat(testDiet.getFood3()).isEqualTo(UPDATED_FOOD_3);
        assertThat(testDiet.getFood4()).isEqualTo(UPDATED_FOOD_4);
        assertThat(testDiet.getFood5()).isEqualTo(UPDATED_FOOD_5);
        assertThat(testDiet.getFood6()).isEqualTo(UPDATED_FOOD_6);
        assertThat(testDiet.getFood7()).isEqualTo(UPDATED_FOOD_7);
        assertThat(testDiet.getFood8()).isEqualTo(UPDATED_FOOD_8);
        assertThat(testDiet.getFood9()).isEqualTo(UPDATED_FOOD_9);
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
