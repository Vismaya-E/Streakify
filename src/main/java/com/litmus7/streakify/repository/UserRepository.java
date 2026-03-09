package com.litmus7.streakify.repository;

import com.litmus7.streakify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long > {

    public boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

}
