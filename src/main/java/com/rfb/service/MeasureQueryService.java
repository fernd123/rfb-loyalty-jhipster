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

import com.rfb.domain.Measure;
import com.rfb.domain.*; // for static metamodels
import com.rfb.repository.MeasureRepository;
import com.rfb.service.dto.MeasureCriteria;

/**
 * Service for executing complex queries for {@link Measure} entities in the database.
 * The main input is a {@link MeasureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Measure} or a {@link Page} of {@link Measure} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MeasureQueryService extends QueryService<Measure> {

    private final Logger log = LoggerFactory.getLogger(MeasureQueryService.class);

    private final MeasureRepository measureRepository;

    public MeasureQueryService(MeasureRepository measureRepository) {
        this.measureRepository = measureRepository;
    }

    /**
     * Return a {@link List} of {@link Measure} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Measure> findByCriteria(MeasureCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Measure> specification = createSpecification(criteria);
        return measureRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Measure} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Measure> findByCriteria(MeasureCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Measure> specification = createSpecification(criteria);
        return measureRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MeasureCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Measure> specification = createSpecification(criteria);
        return measureRepository.count(specification);
    }

    /**
     * Function to convert MeasureCriteria to a {@link Specification}.
     */
    private Specification<Measure> createSpecification(MeasureCriteria criteria) {
        Specification<Measure> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Measure_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Measure_.creationDate));
            }
            if (criteria.getArm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getArm(), Measure_.arm));
            }
            if (criteria.getRibCage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRibCage(), Measure_.ribCage));
            }
            if (criteria.getLeg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLeg(), Measure_.leg));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerId(),
                    root -> root.join(Measure_.customer, JoinType.LEFT).get(Customer_.id)));
            }
        }
        return specification;
    }
}
