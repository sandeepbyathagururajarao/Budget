package com.keyfalcon.budget.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PurchaseSubItem entity.
 */
public class PurchaseSubItemDTO implements Serializable {

    private Long id;

    private String name;

    private String noOfItem;

    private String unit;

    private String price;

    @NotNull
    private String total;

    private String subGpsCoordinate;

    @NotNull
    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    private Long purchaseItemId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoOfItem() {
        return noOfItem;
    }

    public void setNoOfItem(String noOfItem) {
        this.noOfItem = noOfItem;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSubGpsCoordinate() {
        return subGpsCoordinate;
    }

    public void setSubGpsCoordinate(String subGpsCoordinate) {
        this.subGpsCoordinate = subGpsCoordinate;
    }


    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getPurchaseItemId() {
        return purchaseItemId;
    }

    public void setPurchaseItemId(Long purchaseItemId) {
        this.purchaseItemId = purchaseItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PurchaseSubItemDTO purchaseSubItemDTO = (PurchaseSubItemDTO) o;
        if(purchaseSubItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchaseSubItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PurchaseSubItemDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", noOfItem='" + getNoOfItem() + "'" +
            ", unit='" + getUnit() + "'" +
            ", price='" + getPrice() + "'" +
            ", total='" + getTotal() + "'" +
            ", subGpsCoordinate='" + getSubGpsCoordinate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
