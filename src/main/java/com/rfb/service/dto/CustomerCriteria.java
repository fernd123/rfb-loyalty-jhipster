package com.rfb.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.rfb.domain.enumeration.Sex;
import com.rfb.domain.enumeration.Goal;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.rfb.domain.Customer} entity. This class is used
 * in {@link com.rfb.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomerCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Sex
     */
    public static class SexFilter extends Filter<Sex> {

        public SexFilter() {
        }

        public SexFilter(SexFilter filter) {
            super(filter);
        }

        @Override
        public SexFilter copy() {
            return new SexFilter(this);
        }

    }
    /**
     * Class for filtering Goal
     */
    public static class GoalFilter extends Filter<Goal> {

        public GoalFilter() {
        }

        public GoalFilter(GoalFilter filter) {
            super(filter);
        }

        @Override
        public GoalFilter copy() {
            return new GoalFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter firstName;

    private InstantFilter birthDate;

    private SexFilter sex;

    private IntegerFilter phone;

    private StringFilter email;

    private GoalFilter objective;

    private StringFilter observations;

    private InstantFilter creationDate;

    private BooleanFilter isActive;

    private LongFilter customerMeasureId;

    private LongFilter customerTrainingId;

    private LongFilter customerDietId;

    private LongFilter customerDateId;

    public CustomerCriteria(){
    }

    public CustomerCriteria(CustomerCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.sex = other.sex == null ? null : other.sex.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.objective = other.objective == null ? null : other.objective.copy();
        this.observations = other.observations == null ? null : other.observations.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.customerMeasureId = other.customerMeasureId == null ? null : other.customerMeasureId.copy();
        this.customerTrainingId = other.customerTrainingId == null ? null : other.customerTrainingId.copy();
        this.customerDietId = other.customerDietId == null ? null : other.customerDietId.copy();
        this.customerDateId = other.customerDateId == null ? null : other.customerDateId.copy();
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public InstantFilter getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(InstantFilter birthDate) {
        this.birthDate = birthDate;
    }

    public SexFilter getSex() {
        return sex;
    }

    public void setSex(SexFilter sex) {
        this.sex = sex;
    }

    public IntegerFilter getPhone() {
        return phone;
    }

    public void setPhone(IntegerFilter phone) {
        this.phone = phone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public GoalFilter getObjective() {
        return objective;
    }

    public void setObjective(GoalFilter objective) {
        this.objective = objective;
    }

    public StringFilter getObservations() {
        return observations;
    }

    public void setObservations(StringFilter observations) {
        this.observations = observations;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getCustomerMeasureId() {
        return customerMeasureId;
    }

    public void setCustomerMeasureId(LongFilter customerMeasureId) {
        this.customerMeasureId = customerMeasureId;
    }

    public LongFilter getCustomerTrainingId() {
        return customerTrainingId;
    }

    public void setCustomerTrainingId(LongFilter customerTrainingId) {
        this.customerTrainingId = customerTrainingId;
    }

    public LongFilter getCustomerDietId() {
        return customerDietId;
    }

    public void setCustomerDietId(LongFilter customerDietId) {
        this.customerDietId = customerDietId;
    }

    public LongFilter getCustomerDateId() {
        return customerDateId;
    }

    public void setCustomerDateId(LongFilter customerDateId) {
        this.customerDateId = customerDateId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerCriteria that = (CustomerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(sex, that.sex) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(objective, that.objective) &&
            Objects.equals(observations, that.observations) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(customerMeasureId, that.customerMeasureId) &&
            Objects.equals(customerTrainingId, that.customerTrainingId) &&
            Objects.equals(customerDietId, that.customerDietId) &&
            Objects.equals(customerDateId, that.customerDateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        firstName,
        birthDate,
        sex,
        phone,
        email,
        objective,
        observations,
        creationDate,
        isActive,
        customerMeasureId,
        customerTrainingId,
        customerDietId,
        customerDateId
        );
    }

    @Override
    public String toString() {
        return "CustomerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
                (sex != null ? "sex=" + sex + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (objective != null ? "objective=" + objective + ", " : "") +
                (observations != null ? "observations=" + observations + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (customerMeasureId != null ? "customerMeasureId=" + customerMeasureId + ", " : "") +
                (customerTrainingId != null ? "customerTrainingId=" + customerTrainingId + ", " : "") +
                (customerDietId != null ? "customerDietId=" + customerDietId + ", " : "") +
                (customerDateId != null ? "customerDateId=" + customerDateId + ", " : "") +
            "}";
    }

}
