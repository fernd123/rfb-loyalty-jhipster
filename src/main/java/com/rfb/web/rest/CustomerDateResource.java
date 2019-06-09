package com.rfb.web.rest;

import com.rfb.domain.CustomerDate;
import com.rfb.repository.CustomerDateRepository;
import com.rfb.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.rfb.domain.CustomerDate}.
 */
@RestController
@RequestMapping("/api")
public class CustomerDateResource {

    private final Logger log = LoggerFactory.getLogger(CustomerDateResource.class);

    private static final String ENTITY_NAME = "customerDate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerDateRepository customerDateRepository;

    public CustomerDateResource(CustomerDateRepository customerDateRepository) {
        this.customerDateRepository = customerDateRepository;
    }

    /**
     * {@code POST  /customer-dates} : Create a new customerDate.
     *
     * @param customerDate the customerDate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerDate, or with status {@code 400 (Bad Request)} if the customerDate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-dates")
    public ResponseEntity<CustomerDate> createCustomerDate(@RequestBody CustomerDate customerDate) throws URISyntaxException {
        log.debug("REST request to save CustomerDate : {}", customerDate);
        if (customerDate.getId() != null) {
            throw new BadRequestAlertException("A new customerDate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerDate result = customerDateRepository.save(customerDate);
        return ResponseEntity.created(new URI("/api/customer-dates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-dates} : Updates an existing customerDate.
     *
     * @param customerDate the customerDate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDate,
     * or with status {@code 400 (Bad Request)} if the customerDate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerDate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-dates")
    public ResponseEntity<CustomerDate> updateCustomerDate(@RequestBody CustomerDate customerDate) throws URISyntaxException {
        log.debug("REST request to update CustomerDate : {}", customerDate);
        if (customerDate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CustomerDate result = customerDateRepository.save(customerDate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerDate.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /customer-dates} : get all the customerDates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerDates in body.
     */
    @GetMapping("/customer-dates")
    public List<CustomerDate> getAllCustomerDates() {
        log.debug("REST request to get all CustomerDates");
        return customerDateRepository.findAll();
    }

    /**
     * {@code GET  /customer-dates/:id} : get the "id" customerDate.
     *
     * @param id the id of the customerDate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerDate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-dates/{id}")
    public ResponseEntity<CustomerDate> getCustomerDate(@PathVariable Long id) {
        log.debug("REST request to get CustomerDate : {}", id);
        Optional<CustomerDate> customerDate = customerDateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(customerDate);
    }

    /**
     * {@code DELETE  /customer-dates/:id} : delete the "id" customerDate.
     *
     * @param id the id of the customerDate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-dates/{id}")
    public ResponseEntity<Void> deleteCustomerDate(@PathVariable Long id) {
        log.debug("REST request to delete CustomerDate : {}", id);
        customerDateRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
