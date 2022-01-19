package com.saimon.repository;

import com.saimon.models.UserApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Muhammad Saimon
 * @since Apr 4/18/21 2:09 AM
 */

@Repository
public interface UserApiRepository extends JpaRepository<UserApi, Long> {
}
