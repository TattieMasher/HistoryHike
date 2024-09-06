package com.historyhike.historyhike_api.controller;

import com.historyhike.historyhike_api.model.User;
import com.historyhike.historyhike_api.repository.UserRepository;
import com.historyhike.historyhike_api.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;


    @GetMapping("/all")
    private ResponseEntity<List<User>> getAll() {
        List<User> users = userRepository.findAll();
        if (users == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> updates) {
        // Extract the JWT token from the Authorization header
        String jwt = token.substring(7);
        String email = jwtUtils.extractUsername(jwt);

        // Find the user by email
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Update fields if provided
        if (updates.containsKey("firstName")) {
            user.setFirstName((String) updates.get("firstName"));
        }
        if (updates.containsKey("surname")) {
            user.setSurname((String) updates.get("surname"));
        }
        if (updates.containsKey("email")) {
            user.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("passwordHash")) {
            // Encode the supplied password before saving
            user.setPasswordHash(passwordEncoder.encode((String) updates.get("passwordHash")));
        }

        // Save updated user
        User updatedUser = userRepository.save(user);

        return ResponseEntity.ok(updatedUser);
    }

    // Get all details of the authenticated user (excluding password)
    @GetMapping("/me")
    public ResponseEntity<User> getUserDetails(@RequestHeader("Authorization") String token) {
        // Extract the JWT token from the Authorization header
        String jwt = token.substring(7);
        String email = jwtUtils.extractUsername(jwt);

        // Find the user by email
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Set the passwordHash field to null to exclude it from the response
        user.setPasswordHash(null);

        return ResponseEntity.ok(user);
    }

    // Method to handle password reset
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");

        // Find user by email
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("No user found with this email.");
        }

        // Generate a new random password
        String newPassword = generateRandomPassword(8); // Generate an 8-character password

        // Send the new password to the user's email
        sendEmail(user.getEmail(), newPassword);

        // Update the user's password in the database
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("A new password has been sent to your email.");
    }

    // Helper method to generate a random password
    private String generateRandomPassword(int length) {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charSet.length());
            password.append(charSet.charAt(index));
        }
        return password.toString();
    }

    // Helper method to send an email
    private void sendEmail(String to, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);  // recipient's email
        message.setFrom("hello@alex-mccaughran.net");
        message.setSubject("HistoryHike: Password Reset Request");
        message.setText("Your new password is: " + newPassword);
        mailSender.send(message);
    }

}