package com.keyfalcon.budget.repository;

import com.keyfalcon.budget.domain.PurchaseSubItem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PurchaseSubItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseSubItemRepository extends JpaRepository<PurchaseSubItem, Long> {

}
