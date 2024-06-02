package com.historyhike.historyhike_api.controller;

import com.historyhike.historyhike_api.model.Artefact;
import com.historyhike.historyhike_api.model.Quest;
import com.historyhike.historyhike_api.repository.ArtefactRepository;
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
@RequestMapping("/artefact")
public class ArtefactController {
    @Autowired
    private ArtefactRepository artefactRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Artefact>> getAll() {
        List<Artefact> artefacts = artefactRepository.findAll();
        if (artefacts == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(artefacts);
    }
}