package com.historyhike.historyhike_api.controller;

import com.historyhike.historyhike_api.model.Objective;
import com.historyhike.historyhike_api.repository.ObjectiveRepository;
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
@RequestMapping("/objective")
public class ObjectiveController {
    @Autowired
    private ObjectiveRepository objectiveRepository;

    @GetMapping("/all")
    private ResponseEntity<List<Objective>> getAll() {
        List<Objective> objectives = objectiveRepository.findAll();
        if (objectives == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(objectives);
    }
}