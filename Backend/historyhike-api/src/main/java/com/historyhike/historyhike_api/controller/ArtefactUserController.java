package com.historyhike.historyhike_api.controller;

import com.historyhike.historyhike_api.model.ArtefactUser;
import com.historyhike.historyhike_api.repository.ArtefactUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/artefact_user")
public class ArtefactUserController {
    @Autowired
    private ArtefactUserRepository artefactUserRepository;

    @GetMapping("/all")
    private ResponseEntity<List<ArtefactUser>> getAll() {
        List<ArtefactUser> artefactUsers = artefactUserRepository.findAll();
        if (artefactUsers == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(artefactUsers);
    }
}