package com.rfb.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.rfb.domain.Training;
import com.rfb.domain.*; // for static metamodels
import com.rfb.repository.TrainingRepository;
import com.rfb.service.dto.TrainingCriteria;

/**
 * Service for executing complex queries for {@link Training} entities in the database.
 * The main input is a {@link TrainingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Training} or a {@link Page} of {@link Training} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrainingQueryService extends QueryService<Training> {

    private final Logger log = LoggerFactory.getLogger(TrainingQueryService.class);

    private final TrainingRepository trainingRepository;

    public TrainingQueryService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    /**
     * Return a {@link List} of {@link Training} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Training> findByCriteria(TrainingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Training> specification = createSpecification(criteria);
        return trainingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Training} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Training> findByCriteria(TrainingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Training> specification = createSpecification(criteria);
        return trainingRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrainingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Training> specification = createSpecification(criteria);
        return trainingRepository.count(specification);
    }

    /**
     * Function to convert TrainingCriteria to a {@link Specification}.
     */
    private Specification<Training> createSpecification(TrainingCriteria criteria) {
        Specification<Training> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Training_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Training_.creationDate));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Training_.name));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerId(),
                    root -> root.join(Training_.customer, JoinType.LEFT).get(Customer_.id)));
            }
            if (criteria.getTrainingDayId() != null) {
                specification = specification.and(buildSpecification(criteria.getTrainingDayId(),
                    root -> root.join(Training_.trainingDays, JoinType.LEFT).get(TrainingDay_.id)));
            }
        }
        return specification;
    }
}
