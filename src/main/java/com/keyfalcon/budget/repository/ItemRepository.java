package com.keyfalcon.budget.repository;

import com.keyfalcon.budget.domain.Item;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Item entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    public List<Item> findAllByUserId(Long id);
}
