package com.keyfalcon.budget.repository;

import com.keyfalcon.budget.domain.State;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the State entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    public List<State> findAllByUserId(Long id);
    public List<State> findAllByNameContainsAndUserId(String name,Long id);
    public List<State> findAllByNameContains(String name);
}
