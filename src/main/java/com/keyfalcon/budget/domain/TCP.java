package com.keyfalcon.budget.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A TCP.
 */
@Entity
@Table(name = "tcp")
public class TCP implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "para_no_tcp", nullable = false)
    private String paraNoTCP;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    @ManyToOne
    private UserData user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParaNoTCP() {
        return paraNoTCP;
    }

    public TCP paraNoTCP(String paraNoTCP) {
        this.paraNoTCP = paraNoTCP;
        return this;
    }

    public void setParaNoTCP(String paraNoTCP) {
        this.paraNoTCP = paraNoTCP;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public TCP createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public TCP modifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public UserData getUser() {
        return user;
    }

    public TCP user(UserData userData) {
        this.user = userData;
        return this;
    }

    public void setUser(UserData userData) {
        this.user = userData;
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
        TCP tCP = (TCP) o;
        if (tCP.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tCP.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TCP{" +
            "id=" + getId() +
            ", paraNoTCP='" + getParaNoTCP() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
