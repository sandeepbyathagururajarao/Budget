package com.keyfalcon.budget.repository;

import com.keyfalcon.budget.domain.UserData;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the UserData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    List<UserData> findAllByUserName(String userId);
}
