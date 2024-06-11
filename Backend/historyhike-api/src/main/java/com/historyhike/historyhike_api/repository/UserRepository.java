package com.historyhike.historyhike_api.repository;

import com.historyhike.historyhike_api.model.Objective;
import com.historyhike.historyhike_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
