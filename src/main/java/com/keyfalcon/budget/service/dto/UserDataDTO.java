package com.keyfalcon.budget.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the UserData entity.
 */
public class UserDataDTO implements Serializable {

    private Long id;

    @NotNull
    private String userId;

    @NotNull
    private String userName;

    @NotNull
    private String password;

    @NotNull
    private String userType;

    @NotNull
    private String createdBy;

    @NotNull
    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    private Long stateId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserDataDTO userDataDTO = (UserDataDTO) o;
        if(userDataDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userDataDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserDataDTO{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", userName='" + getUserName() + "'" +
            ", password='" + getPassword() + "'" +
            ", userType='" + getUserType() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
