package com.rfb.web.rest;

import com.rfb.domain.TrainingExercise;
import com.rfb.repository.TrainingExerciseRepository;
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
 * REST controller for managing {@link com.rfb.domain.TrainingExercise}.
 */
@RestController
@RequestMapping("/api")
public class TrainingExerciseResource {

    private final Logger log = LoggerFactory.getLogger(TrainingExerciseResource.class);

    private static final String ENTITY_NAME = "trainingExercise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrainingExerciseRepository trainingExerciseRepository;

    public TrainingExerciseResource(TrainingExerciseRepository trainingExerciseRepository) {
        this.trainingExerciseRepository = trainingExerciseRepository;
    }

    /**
     * {@code POST  /training-exercises} : Create a new trainingExercise.
     *
     * @param trainingExercise the trainingExercise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trainingExercise, or with status {@code 400 (Bad Request)} if the trainingExercise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/training-exercises")
    public ResponseEntity<TrainingExercise> createTrainingExercise(@Valid @RequestBody TrainingExercise trainingExercise) throws URISyntaxException {
        log.debug("REST request to save TrainingExercise : {}", trainingExercise);
        if (trainingExercise.getId() != null) {
            throw new BadRequestAlertException("A new trainingExercise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrainingExercise result = trainingExerciseRepository.save(trainingExercise);
        return ResponseEntity.created(new URI("/api/training-exercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /training-exercises} : Updates an existing trainingExercise.
     *
     * @param trainingExercise the trainingExercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingExercise,
     * or with status {@code 400 (Bad Request)} if the trainingExercise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trainingExercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/training-exercises")
    public ResponseEntity<TrainingExercise> updateTrainingExercise(@Valid @RequestBody TrainingExercise trainingExercise) throws URISyntaxException {
        log.debug("REST request to update TrainingExercise : {}", trainingExercise);
        if (trainingExercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TrainingExercise result = trainingExerciseRepository.save(trainingExercise);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trainingExercise.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /training-exercises} : get all the trainingExercises.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trainingExercises in body.
     */
    @GetMapping("/training-exercises")
    public List<TrainingExercise> getAllTrainingExercises() {
        log.debug("REST request to get all TrainingExercises");
        return trainingExerciseRepository.findAll();
    }

    /**
     * {@code GET  /training-exercises/:id} : get the "id" trainingExercise.
     *
     * @param id the id of the trainingExercise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trainingExercise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/training-exercises/{id}")
    public ResponseEntity<TrainingExercise> getTrainingExercise(@PathVariable Long id) {
        log.debug("REST request to get TrainingExercise : {}", id);
        Optional<TrainingExercise> trainingExercise = trainingExerciseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trainingExercise);
    }

    /**
     * {@code DELETE  /training-exercises/:id} : delete the "id" trainingExercise.
     *
     * @param id the id of the trainingExercise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/training-exercises/{id}")
    public ResponseEntity<Void> deleteTrainingExercise(@PathVariable Long id) {
        log.debug("REST request to delete TrainingExercise : {}", id);
        trainingExerciseRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
