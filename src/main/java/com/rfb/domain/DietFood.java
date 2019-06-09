package com.rfb.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DietFood.
 */
@Entity
@Table(name = "diet_food")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DietFood implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JsonIgnoreProperties("dietFoods")
    private Diet diet;

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

    public DietFood creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public DietFood description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Diet getDiet() {
        return diet;
    }

    public DietFood diet(Diet diet) {
        this.diet = diet;
        return this;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DietFood)) {
            return false;
        }
        return id != null && id.equals(((DietFood) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DietFood{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
