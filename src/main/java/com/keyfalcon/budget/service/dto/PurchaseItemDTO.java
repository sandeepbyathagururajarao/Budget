package com.keyfalcon.budget.service.dto;


import java.time.ZonedDateTime;
import javax.persistence.Lob;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PurchaseItem entity.
 */
public class PurchaseItemDTO implements Serializable {

    private Long id;

    @NotNull
    private String purchaseType;

    private String gpsCoordinate;

    @Lob
    private String justification;

    private String image;

    @NotNull
    private String approvalStatus;

    @NotNull
    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    private Long itemId;

    private Long guidelineId;

    private Long subTypeId;

    private Long tcpId;

    private Long stateId;

    private Long userId;

    private Set<PurchaseSubItemDTO> subItems = new HashSet<>();

    private String itemName;

    private String guidelineName;

    private String subTypeName;

    private String tcpName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getGpsCoordinate() {
        return gpsCoordinate;
    }

    public void setGpsCoordinate(String gpsCoordinate) {
        this.gpsCoordinate = gpsCoordinate;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getGuidelineId() {
        return guidelineId;
    }

    public void setGuidelineId(Long guidelineId) {
        this.guidelineId = guidelineId;
    }

    public Long getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(Long subTypeId) {
        this.subTypeId = subTypeId;
    }

    public Long getTcpId() {
        return tcpId;
    }

    public void setTcpId(Long tCPId) {
        this.tcpId = tCPId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userDataId) {
        this.userId = userDataId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public String getGuidelineName() {
        return guidelineName;
    }

    public void setGuidelineName(String guidelineName) {
        this.guidelineName = guidelineName;
    }

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    public String getTcpName() {
        return tcpName;
    }

    public void setTcpName(String tcpName) {
        this.tcpName = tcpName;
    }

    public void setSubItems(Set<PurchaseSubItemDTO> purchaseSubItems) {
        this.subItems = purchaseSubItems;
    }

    public Set<PurchaseSubItemDTO> getSubItems() {
        return subItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PurchaseItemDTO purchaseItemDTO = (PurchaseItemDTO) o;
        if(purchaseItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchaseItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PurchaseItemDTO{" +
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
