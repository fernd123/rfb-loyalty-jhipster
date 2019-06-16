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

import com.rfb.domain.Diet;
import com.rfb.domain.*; // for static metamodels
import com.rfb.repository.DietRepository;
import com.rfb.service.dto.DietCriteria;

/**
 * Service for executing complex queries for {@link Diet} entities in the database.
 * The main input is a {@link DietCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Diet} or a {@link Page} of {@link Diet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DietQueryService extends QueryService<Diet> {

    private final Logger log = LoggerFactory.getLogger(DietQueryService.class);

    private final DietRepository dietRepository;

    public DietQueryService(DietRepository dietRepository) {
        this.dietRepository = dietRepository;
    }

    /**
     * Return a {@link List} of {@link Diet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Diet> findByCriteria(DietCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Diet> specification = createSpecification(criteria);
        return dietRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Diet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Diet> findByCriteria(DietCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Diet> specification = createSpecification(criteria);
        return dietRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DietCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Diet> specification = createSpecification(criteria);
        return dietRepository.count(specification);
    }

    /**
     * Function to convert DietCriteria to a {@link Specification}.
     */
    private Specification<Diet> createSpecification(DietCriteria criteria) {
        Specification<Diet> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Diet_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Diet_.creationDate));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Diet_.name));
            }
            if (criteria.getFood1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFood1(), Diet_.food1));
            }
            if (criteria.getFood2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFood2(), Diet_.food2));
            }
            if (criteria.getFood3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFood3(), Diet_.food3));
            }
            if (criteria.getFood4() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFood4(), Diet_.food4));
            }
            if (criteria.getFood5() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFood5(), Diet_.food5));
            }
            if (criteria.getFood6() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFood6(), Diet_.food6));
            }
            if (criteria.getFood7() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFood7(), Diet_.food7));
            }
            if (criteria.getFood8() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFood8(), Diet_.food8));
            }
            if (criteria.getFood9() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFood9(), Diet_.food9));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerId(),
                    root -> root.join(Diet_.customer, JoinType.LEFT).get(Customer_.id)));
            }
            if (criteria.getDietFoodId() != null) {
                specification = specification.and(buildSpecification(criteria.getDietFoodId(),
                    root -> root.join(Diet_.dietFoods, JoinType.LEFT).get(DietFood_.id)));
            }
        }
        return specification;
    }
}
