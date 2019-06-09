package com.rfb.web.rest;

import com.rfb.domain.DietFood;
import com.rfb.repository.DietFoodRepository;
import com.rfb.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.rfb.domain.DietFood}.
 */
@RestController
@RequestMapping("/api")
public class DietFoodResource {

    private final Logger log = LoggerFactory.getLogger(DietFoodResource.class);

    private static final String ENTITY_NAME = "dietFood";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DietFoodRepository dietFoodRepository;

    public DietFoodResource(DietFoodRepository dietFoodRepository) {
        this.dietFoodRepository = dietFoodRepository;
    }

    /**
     * {@code POST  /diet-foods} : Create a new dietFood.
     *
     * @param dietFood the dietFood to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dietFood, or with status {@code 400 (Bad Request)} if the dietFood has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/diet-foods")
    public ResponseEntity<DietFood> createDietFood(@Valid @RequestBody DietFood dietFood) throws URISyntaxException {
        log.debug("REST request to save DietFood : {}", dietFood);
        if (dietFood.getId() != null) {
            throw new BadRequestAlertException("A new dietFood cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DietFood result = dietFoodRepository.save(dietFood);
        return ResponseEntity.created(new URI("/api/diet-foods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /diet-foods} : Updates an existing dietFood.
     *
     * @param dietFood the dietFood to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dietFood,
     * or with status {@code 400 (Bad Request)} if the dietFood is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dietFood couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/diet-foods")
    public ResponseEntity<DietFood> updateDietFood(@Valid @RequestBody DietFood dietFood) throws URISyntaxException {
        log.debug("REST request to update DietFood : {}", dietFood);
        if (dietFood.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DietFood result = dietFoodRepository.save(dietFood);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dietFood.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /diet-foods} : get all the dietFoods.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dietFoods in body.
     */
    @GetMapping("/diet-foods")
    public List<DietFood> getAllDietFoods() {
        log.debug("REST request to get all DietFoods");
        return dietFoodRepository.findAll();
    }

    /**
     * {@code GET  /diet-foods/:id} : get the "id" dietFood.
     *
     * @param id the id of the dietFood to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dietFood, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/diet-foods/{id}")
    public ResponseEntity<DietFood> getDietFood(@PathVariable Long id) {
        log.debug("REST request to get DietFood : {}", id);
        Optional<DietFood> dietFood = dietFoodRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dietFood);
    }

    /**
     * {@code DELETE  /diet-foods/:id} : delete the "id" dietFood.
     *
     * @param id the id of the dietFood to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/diet-foods/{id}")
    public ResponseEntity<Void> deleteDietFood(@PathVariable Long id) {
        log.debug("REST request to delete DietFood : {}", id);
        dietFoodRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
