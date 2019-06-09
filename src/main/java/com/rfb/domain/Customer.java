package com.rfb.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.rfb.domain.enumeration.Sex;

import com.rfb.domain.enumeration.Goal;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private Instant birthDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private Sex sex;

    @NotNull
    @Column(name = "phone", nullable = false)
    private Integer phone;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "objective")
    private Goal objective;

    @Column(name = "observations")
    private String observations;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * Customer Relationships
     */
    @ApiModelProperty(value = "Customer Relationships")
    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Measure> customerMeasures = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Training> customerTrainings = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Diet> customerDiets = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CustomerDate> customerDates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Customer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public Customer firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public Customer birthDate(Instant birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public Sex getSex() {
        return sex;
    }

    public Customer sex(Sex sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Integer getPhone() {
        return phone;
    }

    public Customer phone(Integer phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public Customer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Goal getObjective() {
        return objective;
    }

    public Customer objective(Goal objective) {
        this.objective = objective;
        return this;
    }

    public void setObjective(Goal objective) {
        this.objective = objective;
    }

    public String getObservations() {
        return observations;
    }

    public Customer observations(String observations) {
        this.observations = observations;
        return this;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Customer creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Customer isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Measure> getCustomerMeasures() {
        return customerMeasures;
    }

    public Customer customerMeasures(Set<Measure> measures) {
        this.customerMeasures = measures;
        return this;
    }

    public Customer addCustomerMeasure(Measure measure) {
        this.customerMeasures.add(measure);
        measure.setCustomer(this);
        return this;
    }

    public Customer removeCustomerMeasure(Measure measure) {
        this.customerMeasures.remove(measure);
        measure.setCustomer(null);
        return this;
    }

    public void setCustomerMeasures(Set<Measure> measures) {
        this.customerMeasures = measures;
    }

    public Set<Training> getCustomerTrainings() {
        return customerTrainings;
    }

    public Customer customerTrainings(Set<Training> trainings) {
        this.customerTrainings = trainings;
        return this;
    }

    public Customer addCustomerTraining(Training training) {
        this.customerTrainings.add(training);
        training.setCustomer(this);
        return this;
    }

    public Customer removeCustomerTraining(Training training) {
        this.customerTrainings.remove(training);
        training.setCustomer(null);
        return this;
    }

    public void setCustomerTrainings(Set<Training> trainings) {
        this.customerTrainings = trainings;
    }

    public Set<Diet> getCustomerDiets() {
        return customerDiets;
    }

    public Customer customerDiets(Set<Diet> diets) {
        this.customerDiets = diets;
        return this;
    }

    public Customer addCustomerDiet(Diet diet) {
        this.customerDiets.add(diet);
        diet.setCustomer(this);
        return this;
    }

    public Customer removeCustomerDiet(Diet diet) {
        this.customerDiets.remove(diet);
        diet.setCustomer(null);
        return this;
    }

    public void setCustomerDiets(Set<Diet> diets) {
        this.customerDiets = diets;
    }

    public Set<CustomerDate> getCustomerDates() {
        return customerDates;
    }

    public Customer customerDates(Set<CustomerDate> customerDates) {
        this.customerDates = customerDates;
        return this;
    }

    public Customer addCustomerDate(CustomerDate customerDate) {
        this.customerDates.add(customerDate);
        customerDate.setCustomer(this);
        return this;
    }

    public Customer removeCustomerDate(CustomerDate customerDate) {
        this.customerDates.remove(customerDate);
        customerDate.setCustomer(null);
        return this;
    }

    public void setCustomerDates(Set<CustomerDate> customerDates) {
        this.customerDates = customerDates;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", sex='" + getSex() + "'" +
            ", phone=" + getPhone() +
            ", email='" + getEmail() + "'" +
            ", objective='" + getObjective() + "'" +
            ", observations='" + getObservations() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
