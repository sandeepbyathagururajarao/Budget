package com.keyfalcon.budget.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SubType entity.
 */
public class SubTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String subTypeNumber;

    @NotNull
    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubTypeNumber() {
        return subTypeNumber;
    }

    public void setSubTypeNumber(String subTypeNumber) {
        this.subTypeNumber = subTypeNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SubTypeDTO subTypeDTO = (SubTypeDTO) o;
        if(subTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubTypeDTO{" +
            "id=" + getId() +
            ", subTypeNumber='" + getSubTypeNumber() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
