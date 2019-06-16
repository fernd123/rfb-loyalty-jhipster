package com.rfb.service;

import com.rfb.domain.Diet;
import com.rfb.repository.DietRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Diet}.
 */
@Service
@Transactional
public class DietService {

    private final Logger log = LoggerFactory.getLogger(DietService.class);

    private final DietRepository dietRepository;

    public DietService(DietRepository dietRepository) {
        this.dietRepository = dietRepository;
    }

    /**
     * Save a diet.
     *
     * @param diet the entity to save.
     * @return the persisted entity.
     */
    public Diet save(Diet diet) {
        log.debug("Request to save Diet : {}", diet);
        return dietRepository.save(diet);
    }

    /**
     * Get all the diets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Diet> findAll(Pageable pageable) {
        log.debug("Request to get all Diets");
        return dietRepository.findAll(pageable);
    }


    /**
     * Get one diet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Diet> findOne(Long id) {
        log.debug("Request to get Diet : {}", id);
        return dietRepository.findById(id);
    }

    /**
     * Delete the diet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Diet : {}", id);
        dietRepository.deleteById(id);
    }
}
