package com.rfb.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

/**
 * A Diet.
 */
@Entity
@Table(name = "diet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Diet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "food_1")
    private String food1;

    @Column(name = "food_2")
    private String food2;

    @Column(name = "food_3")
    private String food3;

    @Column(name = "food_4")
    private String food4;

    @Column(name = "food_5")
    private String food5;

    @Column(name = "food_6")
    private String food6;

    @Column(name = "food_7")
    private String food7;

    @Column(name = "food_8")
    private String food8;

    @Column(name = "food_9")
    private String food9;

    @ManyToOne
    @JsonIgnoreProperties("diets")
    private Customer customer;

    /**
     * Diet Relationships
     */
    @ApiModelProperty(value = "Diet Relationships")
    @OneToMany(mappedBy = "diet")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DietFood> dietFoods = new HashSet<>();

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

    public Diet creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public Diet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFood1() {
        return food1;
    }

    public Diet food1(String food1) {
        this.food1 = food1;
        return this;
    }

    public void setFood1(String food1) {
        this.food1 = food1;
    }

    public String getFood2() {
        return food2;
    }

    public Diet food2(String food2) {
        this.food2 = food2;
        return this;
    }

    public void setFood2(String food2) {
        this.food2 = food2;
    }

    public String getFood3() {
        return food3;
    }

    public Diet food3(String food3) {
        this.food3 = food3;
        return this;
    }

    public void setFood3(String food3) {
        this.food3 = food3;
    }

    public String getFood4() {
        return food4;
    }

    public Diet food4(String food4) {
        this.food4 = food4;
        return this;
    }

    public void setFood4(String food4) {
        this.food4 = food4;
    }

    public String getFood5() {
        return food5;
    }

    public Diet food5(String food5) {
        this.food5 = food5;
        return this;
    }

    public void setFood5(String food5) {
        this.food5 = food5;
    }

    public String getFood6() {
        return food6;
    }

    public Diet food6(String food6) {
        this.food6 = food6;
        return this;
    }

    public void setFood6(String food6) {
        this.food6 = food6;
    }

    public String getFood7() {
        return food7;
    }

    public Diet food7(String food7) {
        this.food7 = food7;
        return this;
    }

    public void setFood7(String food7) {
        this.food7 = food7;
    }

    public String getFood8() {
        return food8;
    }

    public Diet food8(String food8) {
        this.food8 = food8;
        return this;
    }

    public void setFood8(String food8) {
        this.food8 = food8;
    }

    public String getFood9() {
        return food9;
    }

    public Diet food9(String food9) {
        this.food9 = food9;
        return this;
    }

    public void setFood9(String food9) {
        this.food9 = food9;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Diet customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<DietFood> getDietFoods() {
        return dietFoods;
    }

    public Diet dietFoods(Set<DietFood> dietFoods) {
        this.dietFoods = dietFoods;
        return this;
    }

    public Diet addDietFood(DietFood dietFood) {
        this.dietFoods.add(dietFood);
        dietFood.setDiet(this);
        return this;
    }

    public Diet removeDietFood(DietFood dietFood) {
        this.dietFoods.remove(dietFood);
        dietFood.setDiet(null);
        return this;
    }

    public void setDietFoods(Set<DietFood> dietFoods) {
        this.dietFoods = dietFoods;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Diet)) {
            return false;
        }
        return id != null && id.equals(((Diet) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Diet{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", name='" + getName() + "'" +
            ", food1='" + getFood1() + "'" +
            ", food2='" + getFood2() + "'" +
            ", food3='" + getFood3() + "'" +
            ", food4='" + getFood4() + "'" +
            ", food5='" + getFood5() + "'" +
            ", food6='" + getFood6() + "'" +
            ", food7='" + getFood7() + "'" +
            ", food8='" + getFood8() + "'" +
            ", food9='" + getFood9() + "'" +
            "}";
    }
}
