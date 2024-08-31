package com.historyhike.historyhike_api.repository;

import com.historyhike.historyhike_api.model.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Integer> {
    @Query("SELECT q FROM Quest q WHERE q.id NOT IN (SELECT au.artefact.quest.id FROM ArtefactUser au WHERE au.user.id = :userId)")
    List<Quest> findUncompletedQuests(int userId);
}
