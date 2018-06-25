package com.keyfalcon.budget.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PurchaseItem.
 */
@Entity
@Table(name = "purchase_item")
public class PurchaseItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "purchase_type", nullable = false)
    private String purchaseType;

    @Column(name = "gps_coordinate")
    private String gpsCoordinate;

    @Lob
    @Column(name = "justification")
    private String justification;

    @Column(name = "image")
    private String image;

    @NotNull
    @Column(name = "approval_status", nullable = false)
    private String approvalStatus;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    @OneToMany(mappedBy = "purchaseItem")
    private Set<PurchaseSubItem> subItems = new HashSet<>();

    @ManyToOne
    private Item item;

    @ManyToOne
    private Guideline guideline;

    @ManyToOne
    private SubType subType;

    @ManyToOne
    private TCP tcp;

    @ManyToOne
    private State state;

    @ManyToOne
    private UserData user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public PurchaseItem purchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
        return this;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getGpsCoordinate() {
        return gpsCoordinate;
    }

    public PurchaseItem gpsCoordinate(String gpsCoordinate) {
        this.gpsCoordinate = gpsCoordinate;
        return this;
    }

    public void setGpsCoordinate(String gpsCoordinate) {
        this.gpsCoordinate = gpsCoordinate;
    }

    public String getJustification() {
        return justification;
    }

    public PurchaseItem justification(String justification) {
        this.justification = justification;
        return this;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getImage() {
        return image;
    }

    public PurchaseItem image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public PurchaseItem approvalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
        return this;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public PurchaseItem createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public PurchaseItem modifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Set<PurchaseSubItem> getSubItems() {
        return subItems;
    }

    public PurchaseItem subItems(Set<PurchaseSubItem> purchaseSubItems) {
        this.subItems = purchaseSubItems;
        return this;
    }

    public PurchaseItem addSubItems(PurchaseSubItem purchaseSubItem) {
        this.subItems.add(purchaseSubItem);
        purchaseSubItem.setPurchaseItem(this);
        return this;
    }

    public PurchaseItem removeSubItems(PurchaseSubItem purchaseSubItem) {
        this.subItems.remove(purchaseSubItem);
        purchaseSubItem.setPurchaseItem(null);
        return this;
    }

    public void setSubItems(Set<PurchaseSubItem> purchaseSubItems) {
        this.subItems = purchaseSubItems;
    }

    public Item getItem() {
        return item;
    }

    public PurchaseItem item(Item item) {
        this.item = item;
        return this;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Guideline getGuideline() {
        return guideline;
    }

    public PurchaseItem guideline(Guideline guideline) {
        this.guideline = guideline;
        return this;
    }

    public void setGuideline(Guideline guideline) {
        this.guideline = guideline;
    }

    public SubType getSubType() {
        return subType;
    }

    public PurchaseItem subType(SubType subType) {
        this.subType = subType;
        return this;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
    }

    public TCP getTcp() {
        return tcp;
    }

    public PurchaseItem tcp(TCP tCP) {
        this.tcp = tCP;
        return this;
    }

    public void setTcp(TCP tCP) {
        this.tcp = tCP;
    }

    public State getState() {
        return state;
    }

    public PurchaseItem state(State state) {
        this.state = state;
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public UserData getUser() {
        return user;
    }

    public PurchaseItem user(UserData userData) {
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
        PurchaseItem purchaseItem = (PurchaseItem) o;
        if (purchaseItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchaseItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PurchaseItem{" +
            "id=" + getId() +
            ", purchaseType='" + getPurchaseType() + "'" +
            ", gpsCoordinate='" + getGpsCoordinate() + "'" +
            ", justification='" + getJustification() + "'" +
            ", image='" + getImage() + "'" +
            ", approvalStatus='" + getApprovalStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
