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
 * Criteria class for the {@link com.rfb.domain.Diet} entity. This class is used
 * in {@link com.rfb.web.rest.DietResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /diets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DietCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private StringFilter name;

    private StringFilter food1;

    private StringFilter food2;

    private StringFilter food3;

    private StringFilter food4;

    private StringFilter food5;

    private StringFilter food6;

    private StringFilter food7;

    private StringFilter food8;

    private StringFilter food9;

    private LongFilter customerId;

    private LongFilter dietFoodId;

    public DietCriteria(){
    }

    public DietCriteria(DietCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.food1 = other.food1 == null ? null : other.food1.copy();
        this.food2 = other.food2 == null ? null : other.food2.copy();
        this.food3 = other.food3 == null ? null : other.food3.copy();
        this.food4 = other.food4 == null ? null : other.food4.copy();
        this.food5 = other.food5 == null ? null : other.food5.copy();
        this.food6 = other.food6 == null ? null : other.food6.copy();
        this.food7 = other.food7 == null ? null : other.food7.copy();
        this.food8 = other.food8 == null ? null : other.food8.copy();
        this.food9 = other.food9 == null ? null : other.food9.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.dietFoodId = other.dietFoodId == null ? null : other.dietFoodId.copy();
    }

    @Override
    public DietCriteria copy() {
        return new DietCriteria(this);
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

    public StringFilter getFood1() {
        return food1;
    }

    public void setFood1(StringFilter food1) {
        this.food1 = food1;
    }

    public StringFilter getFood2() {
        return food2;
    }

    public void setFood2(StringFilter food2) {
        this.food2 = food2;
    }

    public StringFilter getFood3() {
        return food3;
    }

    public void setFood3(StringFilter food3) {
        this.food3 = food3;
    }

    public StringFilter getFood4() {
        return food4;
    }

    public void setFood4(StringFilter food4) {
        this.food4 = food4;
    }

    public StringFilter getFood5() {
        return food5;
    }

    public void setFood5(StringFilter food5) {
        this.food5 = food5;
    }

    public StringFilter getFood6() {
        return food6;
    }

    public void setFood6(StringFilter food6) {
        this.food6 = food6;
    }

    public StringFilter getFood7() {
        return food7;
    }

    public void setFood7(StringFilter food7) {
        this.food7 = food7;
    }

    public StringFilter getFood8() {
        return food8;
    }

    public void setFood8(StringFilter food8) {
        this.food8 = food8;
    }

    public StringFilter getFood9() {
        return food9;
    }

    public void setFood9(StringFilter food9) {
        this.food9 = food9;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getDietFoodId() {
        return dietFoodId;
    }

    public void setDietFoodId(LongFilter dietFoodId) {
        this.dietFoodId = dietFoodId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DietCriteria that = (DietCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(name, that.name) &&
            Objects.equals(food1, that.food1) &&
            Objects.equals(food2, that.food2) &&
            Objects.equals(food3, that.food3) &&
            Objects.equals(food4, that.food4) &&
            Objects.equals(food5, that.food5) &&
            Objects.equals(food6, that.food6) &&
            Objects.equals(food7, that.food7) &&
            Objects.equals(food8, that.food8) &&
            Objects.equals(food9, that.food9) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(dietFoodId, that.dietFoodId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        creationDate,
        name,
        food1,
        food2,
        food3,
        food4,
        food5,
        food6,
        food7,
        food8,
        food9,
        customerId,
        dietFoodId
        );
    }

    @Override
    public String toString() {
        return "DietCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (food1 != null ? "food1=" + food1 + ", " : "") +
                (food2 != null ? "food2=" + food2 + ", " : "") +
                (food3 != null ? "food3=" + food3 + ", " : "") +
                (food4 != null ? "food4=" + food4 + ", " : "") +
                (food5 != null ? "food5=" + food5 + ", " : "") +
                (food6 != null ? "food6=" + food6 + ", " : "") +
                (food7 != null ? "food7=" + food7 + ", " : "") +
                (food8 != null ? "food8=" + food8 + ", " : "") +
                (food9 != null ? "food9=" + food9 + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
                (dietFoodId != null ? "dietFoodId=" + dietFoodId + ", " : "") +
            "}";
    }

}
