package com.keyfalcon.budget.repository;

import com.keyfalcon.budget.domain.TCP;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TCP entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TCPRepository extends JpaRepository<TCP, Long> {

}
