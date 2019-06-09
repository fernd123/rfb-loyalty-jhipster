package com.rfb.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Measure.
 */
@Entity
@Table(name = "measure")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Measure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "arm")
    private Double arm;

    @Column(name = "rib_cage")
    private Double ribCage;

    @Column(name = "leg")
    private Double leg;

    @ManyToOne
    @JsonIgnoreProperties("measures")
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Measure creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Double getArm() {
        return arm;
    }

    public Measure arm(Double arm) {
        this.arm = arm;
        return this;
    }

    public void setArm(Double arm) {
        this.arm = arm;
    }

    public Double getRibCage() {
        return ribCage;
    }

    public Measure ribCage(Double ribCage) {
        this.ribCage = ribCage;
        return this;
    }

    public void setRibCage(Double ribCage) {
        this.ribCage = ribCage;
    }

    public Double getLeg() {
        return leg;
    }

    public Measure leg(Double leg) {
        this.leg = leg;
        return this;
    }

    public void setLeg(Double leg) {
        this.leg = leg;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Measure customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Measure)) {
            return false;
        }
        return id != null && id.equals(((Measure) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Measure{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", arm=" + getArm() +
            ", ribCage=" + getRibCage() +
            ", leg=" + getLeg() +
            "}";
    }
}
