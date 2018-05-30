package com.keyfalcon.budget.repository;

import com.keyfalcon.budget.domain.PurchaseItem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the PurchaseItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
    public List<PurchaseItem> findAllByUserIdAndPurchaseType(Long userId, String purchaseType);
    public List<PurchaseItem> findAllByPurchaseType(String purchaseType);
    public List<PurchaseItem> findAllByApprovalStatusAndPurchaseType(String approvalStatus, String purchaseType);
    public List<PurchaseItem> findAllByApprovalStatusAndUserIdAndPurchaseType(String approvalStatus, Long userId, String purchaseType);
}
