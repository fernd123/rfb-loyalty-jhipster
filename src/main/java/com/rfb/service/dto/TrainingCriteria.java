package com.rfb.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.rfb.domain.Training} entity. This class is used
 * in {@link com.rfb.web.rest.TrainingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trainings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TrainingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private StringFilter name;

    private LongFilter customerId;

    private LongFilter trainingDayId;

    public TrainingCriteria(){
    }

    public TrainingCriteria(TrainingCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.trainingDayId = other.trainingDayId == null ? null : other.trainingDayId.copy();
    }

    @Override
    public TrainingCriteria copy() {
        return new TrainingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getTrainingDayId() {
        return trainingDayId;
    }

    public void setTrainingDayId(LongFilter trainingDayId) {
        this.trainingDayId = trainingDayId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TrainingCriteria that = (TrainingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(name, that.name) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(trainingDayId, that.trainingDayId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        creationDate,
        name,
        customerId,
        trainingDayId
        );
    }

    @Override
    public String toString() {
        return "TrainingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
                (trainingDayId != null ? "trainingDayId=" + trainingDayId + ", " : "") +
            "}";
    }

}
