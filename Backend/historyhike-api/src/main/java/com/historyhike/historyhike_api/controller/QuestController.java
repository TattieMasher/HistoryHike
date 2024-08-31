package com.historyhike.historyhike_api.controller;

import com.historyhike.historyhike_api.model.Quest;
import com.historyhike.historyhike_api.model.User;
import com.historyhike.historyhike_api.repository.QuestRepository;
import com.historyhike.historyhike_api.repository.UserRepository;
import com.historyhike.historyhike_api.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/quest")
public class QuestController {
    @Autowired
    private QuestRepository questRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/all")
    public ResponseEntity<List<Quest>> getAll() {
        List<Quest> quests = questRepository.findAll();
        if (quests == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quests);
    }

    @GetMapping("/uncompleted")
    public ResponseEntity<List<Quest>> getUncompletedQuests(@RequestHeader("Authorization") String token) {
        // Extract the JWT token from the Authorization header
        String jwt = token.substring(7);
        String email = jwtUtils.extractUsername(jwt);

        // Find the user by email
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        // Fetch uncompleted quests for the user
        List<Quest> uncompletedQuests = questRepository.findUncompletedQuests(user.getId());

        return ResponseEntity.ok(uncompletedQuests);
    }
}

