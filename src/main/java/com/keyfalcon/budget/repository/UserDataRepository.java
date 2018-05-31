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
    List<UserData> findAllByUserId(String userId);
    List<UserData> findAllByCreatedByAndUserType(String userId, String criteria);
    List<UserData> findAllByUserType(String criteria);
    Long countByCreatedByAndUserType(String userId, String criteria);
    Long countAllByUserType(String userType);
    Long countByCreatedBy(String userId);
}
