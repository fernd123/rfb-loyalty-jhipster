package com.rfb.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TrainingDay.
 */
@Entity
@Table(name = "training_day")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TrainingDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JsonIgnoreProperties("trainingDays")
    private Training training;

    @OneToMany(mappedBy = "trainingDay")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TrainingExercise> trainingExercises = new HashSet<>();

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

    public TrainingDay creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public TrainingDay name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Training getTraining() {
        return training;
    }

    public TrainingDay training(Training training) {
        this.training = training;
        return this;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public Set<TrainingExercise> getTrainingExercises() {
        return trainingExercises;
    }

    public TrainingDay trainingExercises(Set<TrainingExercise> trainingExercises) {
        this.trainingExercises = trainingExercises;
        return this;
    }

    public TrainingDay addTrainingExercise(TrainingExercise trainingExercise) {
        this.trainingExercises.add(trainingExercise);
        trainingExercise.setTrainingDay(this);
        return this;
    }

    public TrainingDay removeTrainingExercise(TrainingExercise trainingExercise) {
        this.trainingExercises.remove(trainingExercise);
        trainingExercise.setTrainingDay(null);
        return this;
    }

    public void setTrainingExercises(Set<TrainingExercise> trainingExercises) {
        this.trainingExercises = trainingExercises;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrainingDay)) {
            return false;
        }
        return id != null && id.equals(((TrainingDay) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TrainingDay{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
