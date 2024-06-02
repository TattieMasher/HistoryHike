package com.historyhike.historyhike_api.repository;

import com.historyhike.historyhike_api.model.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Integer> {
}
