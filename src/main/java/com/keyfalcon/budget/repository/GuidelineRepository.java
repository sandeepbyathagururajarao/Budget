package com.keyfalcon.budget.repository;

import com.keyfalcon.budget.domain.Guideline;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Guideline entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuidelineRepository extends JpaRepository<Guideline, Long> {

}
