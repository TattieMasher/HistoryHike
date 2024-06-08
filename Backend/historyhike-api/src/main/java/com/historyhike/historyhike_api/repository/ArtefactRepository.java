package com.historyhike.historyhike_api.repository;

import com.historyhike.historyhike_api.model.Artefact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtefactRepository extends JpaRepository<Artefact, Integer> {
    Artefact findByQuestId(int questId);
}
