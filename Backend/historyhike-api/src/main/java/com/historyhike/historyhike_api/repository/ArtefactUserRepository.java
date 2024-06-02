package com.historyhike.historyhike_api.repository;

import com.historyhike.historyhike_api.model.ArtefactUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtefactUserRepository extends JpaRepository<ArtefactUser, Integer> {
}
