package com.cart.ecom_proj.controller;

import com.cart.ecom_proj.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        try {
            if (!jwtUtil.isTokenValid(authentication)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid authentication token");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            }

            // Extract user information from JWT token
            String username = jwtUtil.getUsernameFromToken(authentication);
            String email = jwtUtil.getEmailFromToken(authentication);
            String name = jwtUtil.getNameFromToken(authentication);
            String phoneNumber = jwtUtil.getPhoneNumberFromToken(authentication);
            String country = jwtUtil.getCountryFromToken(authentication);

            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("username", username);
            userProfile.put("email", email);
            userProfile.put("name", name);
            userProfile.put("phoneNumber", phoneNumber);
            userProfile.put("country", country);

            Map<String, Object> response = new HashMap<>();
            response.put("user", userProfile);
            response.put("message", "User profile retrieved successfully");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve user profile: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
