package com.keyfalcon.budget.repository;

import com.keyfalcon.budget.domain.Guideline;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Guideline entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuidelineRepository extends JpaRepository<Guideline, Long> {
    public List<Guideline> findAllByUserId(Long id);
    public List<Guideline> findAllByParaNameContainsAndUserId(String guideLineName,Long id);
    public List<Guideline> findAllByParaNameContains(String guideLineName);
}
