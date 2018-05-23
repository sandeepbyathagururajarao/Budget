package com.keyfalcon.budget.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A PurchaseSubItem.
 */
@Entity
@Table(name = "purchase_sub_item")
public class PurchaseSubItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "no_of_item")
    private String noOfItem;

    @Column(name = "unit")
    private String unit;

    @Column(name = "price")
    private String price;

    @NotNull
    @Column(name = "total", nullable = false)
    private String total;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    @ManyToOne
    private PurchaseItem purchaseItem;

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

    public PurchaseSubItem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoOfItem() {
        return noOfItem;
    }

    public PurchaseSubItem noOfItem(String noOfItem) {
        this.noOfItem = noOfItem;
        return this;
    }

    public void setNoOfItem(String noOfItem) {
        this.noOfItem = noOfItem;
    }

    public String getUnit() {
        return unit;
    }

    public PurchaseSubItem unit(String unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public PurchaseSubItem price(String price) {
        this.price = price;
        return this;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public PurchaseSubItem total(String total) {
        this.total = total;
        return this;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public PurchaseSubItem createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public PurchaseSubItem modifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public PurchaseItem getPurchaseItem() {
        return purchaseItem;
    }

    public PurchaseSubItem purchaseItem(PurchaseItem purchaseItem) {
        this.purchaseItem = purchaseItem;
        return this;
    }

    public void setPurchaseItem(PurchaseItem purchaseItem) {
        this.purchaseItem = purchaseItem;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PurchaseSubItem purchaseSubItem = (PurchaseSubItem) o;
        if (purchaseSubItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchaseSubItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PurchaseSubItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", noOfItem='" + getNoOfItem() + "'" +
            ", unit='" + getUnit() + "'" +
            ", price='" + getPrice() + "'" +
            ", total='" + getTotal() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
