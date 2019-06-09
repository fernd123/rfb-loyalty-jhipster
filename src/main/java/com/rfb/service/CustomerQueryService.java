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

import com.rfb.domain.Customer;
import com.rfb.domain.*; // for static metamodels
import com.rfb.repository.CustomerRepository;
import com.rfb.service.dto.CustomerCriteria;

/**
 * Service for executing complex queries for {@link Customer} entities in the database.
 * The main input is a {@link CustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Customer} or a {@link Page} of {@link Customer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerQueryService extends QueryService<Customer> {

    private final Logger log = LoggerFactory.getLogger(CustomerQueryService.class);

    private final CustomerRepository customerRepository;

    public CustomerQueryService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Return a {@link List} of {@link Customer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Customer> findByCriteria(CustomerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Customer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Customer> findByCriteria(CustomerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.count(specification);
    }

    /**
     * Function to convert CustomerCriteria to a {@link Specification}.
     */
    private Specification<Customer> createSpecification(CustomerCriteria criteria) {
        Specification<Customer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Customer_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Customer_.name));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Customer_.firstName));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), Customer_.birthDate));
            }
            if (criteria.getSex() != null) {
                specification = specification.and(buildSpecification(criteria.getSex(), Customer_.sex));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPhone(), Customer_.phone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Customer_.email));
            }
            if (criteria.getObjective() != null) {
                specification = specification.and(buildSpecification(criteria.getObjective(), Customer_.objective));
            }
            if (criteria.getObservations() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservations(), Customer_.observations));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Customer_.creationDate));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Customer_.isActive));
            }
            if (criteria.getCustomerMeasureId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerMeasureId(),
                    root -> root.join(Customer_.customerMeasures, JoinType.LEFT).get(Measure_.id)));
            }
            if (criteria.getCustomerTrainingId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerTrainingId(),
                    root -> root.join(Customer_.customerTrainings, JoinType.LEFT).get(Training_.id)));
            }
            if (criteria.getCustomerDietId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerDietId(),
                    root -> root.join(Customer_.customerDiets, JoinType.LEFT).get(Diet_.id)));
            }
            if (criteria.getCustomerDateId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerDateId(),
                    root -> root.join(Customer_.customerDates, JoinType.LEFT).get(CustomerDate_.id)));
            }
        }
        return specification;
    }
}
