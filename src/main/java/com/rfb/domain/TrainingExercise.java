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
 * A TrainingExercise.
 */
@Entity
@Table(name = "training_exercise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TrainingExercise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @NotNull
    @Column(name = "training_number", nullable = false)
    private Integer trainingNumber;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties("trainingExercises")
    private TrainingDay trainingDay;

    @ManyToOne
    @JsonIgnoreProperties("trainingExercises")
    private Exercise exercise;

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

    public TrainingExercise creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getTrainingNumber() {
        return trainingNumber;
    }

    public TrainingExercise trainingNumber(Integer trainingNumber) {
        this.trainingNumber = trainingNumber;
        return this;
    }

    public void setTrainingNumber(Integer trainingNumber) {
        this.trainingNumber = trainingNumber;
    }

    public String getDescription() {
        return description;
    }

    public TrainingExercise description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TrainingDay getTrainingDay() {
        return trainingDay;
    }

    public TrainingExercise trainingDay(TrainingDay trainingDay) {
        this.trainingDay = trainingDay;
        return this;
    }

    public void setTrainingDay(TrainingDay trainingDay) {
        this.trainingDay = trainingDay;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public TrainingExercise exercise(Exercise exercise) {
        this.exercise = exercise;
        return this;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrainingExercise)) {
            return false;
        }
        return id != null && id.equals(((TrainingExercise) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TrainingExercise{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", trainingNumber=" + getTrainingNumber() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
