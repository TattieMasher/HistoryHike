package com.historyhike.historyhike_api.controller;

import com.historyhike.historyhike_api.model.Quest;
import com.historyhike.historyhike_api.repository.QuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/quest")
public class QuestController {
    @Autowired
    private QuestRepository questRepository;

    @GetMapping("/all")
    private ResponseEntity<List<Quest>> getAll() {
        List<Quest> quests = questRepository.findAll();
        if(quests == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quests);
    }
}
