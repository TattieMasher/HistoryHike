package com.historyhike.historyhike_api.controller;

import com.historyhike.historyhike_api.model.Artefact;
import com.historyhike.historyhike_api.model.ArtefactUser;
import com.historyhike.historyhike_api.model.User;
import com.historyhike.historyhike_api.repository.ArtefactRepository;
import com.historyhike.historyhike_api.repository.ArtefactUserRepository;
import com.historyhike.historyhike_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/artefact_user")
public class ArtefactUserController {
    @Autowired
    private ArtefactUserRepository artefactUserRepository;
    private ArtefactRepository artefactRepository;
    private UserRepository userRepository;

    @GetMapping("/all")
    private ResponseEntity<List<ArtefactUser>> getAll() {
        List<ArtefactUser> artefactUsers = artefactUserRepository.findAll();
        if (artefactUsers == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(artefactUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtefactUser> getById(@PathVariable int id) {
        ArtefactUser artefactUser = artefactUserRepository.findById(id).orElse(null);
        if (artefactUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(artefactUser);
    }

    // Create a new artefact_user entry for completing a quest
    @PostMapping("/complete_quest")
    public ResponseEntity<ArtefactUser> createArtefactUser(@RequestParam int userId, @RequestParam int questId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Artefact artefact = artefactRepository.findByQuestId(questId);
        if (artefact == null) {
            return ResponseEntity.badRequest().body(null);
        }

        ArtefactUser artefactUser = new ArtefactUser();
        artefactUser.setUser(user);
        artefactUser.setArtefact(artefact);
        artefactUser.setCollectedDate(java.time.LocalDateTime.now());

        ArtefactUser savedArtefactUser = artefactUserRepository.save(artefactUser);
        return ResponseEntity.ok(savedArtefactUser);
    }
}