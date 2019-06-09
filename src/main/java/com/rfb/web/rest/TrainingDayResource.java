package com.rfb.web.rest;

import com.rfb.domain.TrainingDay;
import com.rfb.repository.TrainingDayRepository;
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
 * REST controller for managing {@link com.rfb.domain.TrainingDay}.
 */
@RestController
@RequestMapping("/api")
public class TrainingDayResource {

    private final Logger log = LoggerFactory.getLogger(TrainingDayResource.class);

    private static final String ENTITY_NAME = "trainingDay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrainingDayRepository trainingDayRepository;

    public TrainingDayResource(TrainingDayRepository trainingDayRepository) {
        this.trainingDayRepository = trainingDayRepository;
    }

    /**
     * {@code POST  /training-days} : Create a new trainingDay.
     *
     * @param trainingDay the trainingDay to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trainingDay, or with status {@code 400 (Bad Request)} if the trainingDay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/training-days")
    public ResponseEntity<TrainingDay> createTrainingDay(@Valid @RequestBody TrainingDay trainingDay) throws URISyntaxException {
        log.debug("REST request to save TrainingDay : {}", trainingDay);
        if (trainingDay.getId() != null) {
            throw new BadRequestAlertException("A new trainingDay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrainingDay result = trainingDayRepository.save(trainingDay);
        return ResponseEntity.created(new URI("/api/training-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /training-days} : Updates an existing trainingDay.
     *
     * @param trainingDay the trainingDay to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingDay,
     * or with status {@code 400 (Bad Request)} if the trainingDay is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trainingDay couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/training-days")
    public ResponseEntity<TrainingDay> updateTrainingDay(@Valid @RequestBody TrainingDay trainingDay) throws URISyntaxException {
        log.debug("REST request to update TrainingDay : {}", trainingDay);
        if (trainingDay.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TrainingDay result = trainingDayRepository.save(trainingDay);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trainingDay.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /training-days} : get all the trainingDays.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trainingDays in body.
     */
    @GetMapping("/training-days")
    public List<TrainingDay> getAllTrainingDays() {
        log.debug("REST request to get all TrainingDays");
        return trainingDayRepository.findAll();
    }

    /**
     * {@code GET  /training-days/:id} : get the "id" trainingDay.
     *
     * @param id the id of the trainingDay to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trainingDay, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/training-days/{id}")
    public ResponseEntity<TrainingDay> getTrainingDay(@PathVariable Long id) {
        log.debug("REST request to get TrainingDay : {}", id);
        Optional<TrainingDay> trainingDay = trainingDayRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trainingDay);
    }

    /**
     * {@code DELETE  /training-days/:id} : delete the "id" trainingDay.
     *
     * @param id the id of the trainingDay to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/training-days/{id}")
    public ResponseEntity<Void> deleteTrainingDay(@PathVariable Long id) {
        log.debug("REST request to delete TrainingDay : {}", id);
        trainingDayRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
