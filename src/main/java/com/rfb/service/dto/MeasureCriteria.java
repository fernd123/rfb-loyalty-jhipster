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
 * Criteria class for the {@link com.rfb.domain.Measure} entity. This class is used
 * in {@link com.rfb.web.rest.MeasureResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /measures?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MeasureCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private DoubleFilter arm;

    private DoubleFilter ribCage;

    private DoubleFilter leg;

    private LongFilter customerId;

    public MeasureCriteria(){
    }

    public MeasureCriteria(MeasureCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.arm = other.arm == null ? null : other.arm.copy();
        this.ribCage = other.ribCage == null ? null : other.ribCage.copy();
        this.leg = other.leg == null ? null : other.leg.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
    }

    @Override
    public MeasureCriteria copy() {
        return new MeasureCriteria(this);
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

    public DoubleFilter getArm() {
        return arm;
    }

    public void setArm(DoubleFilter arm) {
        this.arm = arm;
    }

    public DoubleFilter getRibCage() {
        return ribCage;
    }

    public void setRibCage(DoubleFilter ribCage) {
        this.ribCage = ribCage;
    }

    public DoubleFilter getLeg() {
        return leg;
    }

    public void setLeg(DoubleFilter leg) {
        this.leg = leg;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MeasureCriteria that = (MeasureCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(arm, that.arm) &&
            Objects.equals(ribCage, that.ribCage) &&
            Objects.equals(leg, that.leg) &&
            Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        creationDate,
        arm,
        ribCage,
        leg,
        customerId
        );
    }

    @Override
    public String toString() {
        return "MeasureCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (arm != null ? "arm=" + arm + ", " : "") +
                (ribCage != null ? "ribCage=" + ribCage + ", " : "") +
                (leg != null ? "leg=" + leg + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
            "}";
    }

}
