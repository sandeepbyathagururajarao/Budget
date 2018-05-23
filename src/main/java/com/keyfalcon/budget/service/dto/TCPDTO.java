package com.keyfalcon.budget.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TCP entity.
 */
public class TCPDTO implements Serializable {

    private Long id;

    @NotNull
    private String paraNoTCP;

    @NotNull
    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParaNoTCP() {
        return paraNoTCP;
    }

    public void setParaNoTCP(String paraNoTCP) {
        this.paraNoTCP = paraNoTCP;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userDataId) {
        this.userId = userDataId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TCPDTO tCPDTO = (TCPDTO) o;
        if(tCPDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tCPDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TCPDTO{" +
            "id=" + getId() +
            ", paraNoTCP='" + getParaNoTCP() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
