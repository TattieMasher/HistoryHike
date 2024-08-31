package com.historyhike.historyhike_api.repository;

import com.historyhike.historyhike_api.model.Artefact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtefactRepository extends JpaRepository<Artefact, Integer> {
    Artefact findByQuestId(int questId);
    @Query("SELECT au.artefact FROM ArtefactUser au WHERE au.user.id = :userId")
    List<Artefact> findArtefactsByUserId(int userId);
}
