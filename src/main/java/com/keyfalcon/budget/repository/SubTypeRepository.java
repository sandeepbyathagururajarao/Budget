package com.keyfalcon.budget.repository;

import com.keyfalcon.budget.domain.SubType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the SubType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubTypeRepository extends JpaRepository<SubType, Long> {
    public List<SubType> findAllByUserId(Long id);
}
