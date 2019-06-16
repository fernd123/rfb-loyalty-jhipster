package com.rfb.web.rest;

import com.rfb.domain.Diet;
import com.rfb.service.DietService;
import com.rfb.web.rest.errors.BadRequestAlertException;
import com.rfb.service.dto.DietCriteria;
import com.rfb.service.DietQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.rfb.domain.Diet}.
 */
@RestController
@RequestMapping("/api")
public class DietResource {

    private final Logger log = LoggerFactory.getLogger(DietResource.class);

    private static final String ENTITY_NAME = "diet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DietService dietService;

    private final DietQueryService dietQueryService;

    public DietResource(DietService dietService, DietQueryService dietQueryService) {
        this.dietService = dietService;
        this.dietQueryService = dietQueryService;
    }

    /**
     * {@code POST  /diets} : Create a new diet.
     *
     * @param diet the diet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new diet, or with status {@code 400 (Bad Request)} if the diet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/diets")
    public ResponseEntity<Diet> createDiet(@Valid @RequestBody Diet diet) throws URISyntaxException {
        log.debug("REST request to save Diet : {}", diet);
        if (diet.getId() != null) {
            throw new BadRequestAlertException("A new diet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Diet result = dietService.save(diet);
        return ResponseEntity.created(new URI("/api/diets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /diets} : Updates an existing diet.
     *
     * @param diet the diet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diet,
     * or with status {@code 400 (Bad Request)} if the diet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the diet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/diets")
    public ResponseEntity<Diet> updateDiet(@Valid @RequestBody Diet diet) throws URISyntaxException {
        log.debug("REST request to update Diet : {}", diet);
        if (diet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Diet result = dietService.save(diet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, diet.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /diets} : get all the diets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of diets in body.
     */
    @GetMapping("/diets")
    public ResponseEntity<List<Diet>> getAllDiets(DietCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get Diets by criteria: {}", criteria);
        Page<Diet> page = dietQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /diets/count} : count all the diets.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/diets/count")
    public ResponseEntity<Long> countDiets(DietCriteria criteria) {
        log.debug("REST request to count Diets by criteria: {}", criteria);
        return ResponseEntity.ok().body(dietQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /diets/:id} : get the "id" diet.
     *
     * @param id the id of the diet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the diet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/diets/{id}")
    public ResponseEntity<Diet> getDiet(@PathVariable Long id) {
        log.debug("REST request to get Diet : {}", id);
        Optional<Diet> diet = dietService.findOne(id);
        return ResponseUtil.wrapOrNotFound(diet);
    }

    /**
     * {@code DELETE  /diets/:id} : delete the "id" diet.
     *
     * @param id the id of the diet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/diets/{id}")
    public ResponseEntity<Void> deleteDiet(@PathVariable Long id) {
        log.debug("REST request to delete Diet : {}", id);
        dietService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
