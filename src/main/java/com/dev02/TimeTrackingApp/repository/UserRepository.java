package com.dev02.TimeTrackingApp.repository;

import com.dev02.TimeTrackingApp.entity.User;
import com.dev02.TimeTrackingApp.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository <User, Long> {
    User findByUsernameEquals(String username);

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);

    boolean existsByEmail(String email);

    @Query(value = "SELECT COUNT(u) FROM User u WHERE u.userRole.roleType = ?1")
    long countAdmin(RoleType roleType);
}
