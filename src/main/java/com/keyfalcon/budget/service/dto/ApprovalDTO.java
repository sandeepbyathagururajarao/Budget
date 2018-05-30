package com.keyfalcon.budget.service.dto;

import java.io.Serializable;

/**
 * Approval DTO
 */

public class ApprovalDTO  implements Serializable {

    private Long id = null;
    private String approvalStatus = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
