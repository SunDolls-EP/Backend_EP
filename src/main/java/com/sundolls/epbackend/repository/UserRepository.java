package com.sundolls.epbackend.repository;

import com.sundolls.epbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {
     Optional<User> findByEmail(String email);
    List<User> findByUsername(String username);

    Optional<User> findByUsernameAndTag(String username, String tag);

    List<User> findAllByUsernameOrderByTagAsc(String username);

    @Query(
            value = "SELECT * FROM user_tb ORDER BY RAND() LIMIT :limit",
            nativeQuery = true
    )
    List<User> findByRandom(@Param("limit") Integer limit);

    Optional<User> findById(String id);
}