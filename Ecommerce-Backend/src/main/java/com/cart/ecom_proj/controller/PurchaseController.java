package com.cart.ecom_proj.controller;

import com.cart.ecom_proj.model.Purchase;
import com.cart.ecom_proj.service.PurchaseService;
import com.cart.ecom_proj.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> createPurchase(@Valid @RequestBody Purchase purchase, Authentication authentication) {
        try {
            if (!jwtUtil.isTokenValid(authentication)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid authentication token");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            }

            // Set username from JWT token
            String username = jwtUtil.getUsernameFromToken(authentication);
            if (username == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Unable to extract username from token");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            }
            
            purchase.setUsername(username);
            Purchase createdPurchase = purchaseService.createPurchase(purchase);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Purchase created successfully");
            response.put("purchase", createdPurchase);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create purchase: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getAllPurchasesByUsername(@PathVariable String username, Authentication authentication) {
        try {
            if (!jwtUtil.isTokenValid(authentication)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid authentication token");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            }

            String tokenUsername = jwtUtil.getUsernameFromToken(authentication);
            if (!username.equals(tokenUsername)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Access denied: You can only view your own purchases");
                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
            }

            List<Purchase> purchases = purchaseService.getAllPurchasesByUsername(username);
            Map<String, Object> response = new HashMap<>();
            response.put("purchases", purchases);
            response.put("count", purchases.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve purchases: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{username}/upcoming")
    public ResponseEntity<?> getUpcomingDeliveries(@PathVariable String username, Authentication authentication) {
        try {
            if (!jwtUtil.isTokenValid(authentication)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid authentication token");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            }

            String tokenUsername = jwtUtil.getUsernameFromToken(authentication);
            if (!username.equals(tokenUsername)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Access denied: You can only view your own purchases");
                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
            }

            List<Purchase> purchases = purchaseService.getUpcomingDeliveries(username);
            Map<String, Object> response = new HashMap<>();
            response.put("upcomingDeliveries", purchases);
            response.put("count", purchases.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve upcoming deliveries: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{username}/past")
    public ResponseEntity<?> getPastDeliveries(@PathVariable String username, Authentication authentication) {
        try {
            if (!jwtUtil.isTokenValid(authentication)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid authentication token");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            }

            String tokenUsername = jwtUtil.getUsernameFromToken(authentication);
            if (!username.equals(tokenUsername)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Access denied: You can only view your own purchases");
                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
            }

            List<Purchase> purchases = purchaseService.getPastDeliveries(username);
            Map<String, Object> response = new HashMap<>();
            response.put("pastDeliveries", purchases);
            response.put("count", purchases.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve past deliveries: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPurchaseById(@PathVariable Long id, Authentication authentication) {
        try {
            if (!jwtUtil.isTokenValid(authentication)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid authentication token");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            }

            Optional<Purchase> purchase = purchaseService.getPurchaseById(id);
            if (purchase.isPresent()) {
                String tokenUsername = jwtUtil.getUsernameFromToken(authentication);
                if (!purchase.get().getUsername().equals(tokenUsername)) {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Access denied: You can only view your own purchases");
                    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
                }
                return new ResponseEntity<>(purchase.get(), HttpStatus.OK);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Purchase not found");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve purchase: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePurchase(@PathVariable Long id, @Valid @RequestBody Purchase purchaseDetails, Authentication authentication) {
        try {
            if (!jwtUtil.isTokenValid(authentication)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid authentication token");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            }

            String tokenUsername = jwtUtil.getUsernameFromToken(authentication);
            if (!purchaseService.isPurchaseOwnedByUser(id, tokenUsername)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Access denied: You can only update your own purchases");
                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
            }

            Purchase updatedPurchase = purchaseService.updatePurchase(id, purchaseDetails);
            if (updatedPurchase != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Purchase updated successfully");
                response.put("purchase", updatedPurchase);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Purchase not found");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update purchase: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePurchase(@PathVariable Long id, Authentication authentication) {
        try {
            if (!jwtUtil.isTokenValid(authentication)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid authentication token");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            }

            String tokenUsername = jwtUtil.getUsernameFromToken(authentication);
            if (!purchaseService.isPurchaseOwnedByUser(id, tokenUsername)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Access denied: You can only delete your own purchases");
                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
            }

            boolean deleted = purchaseService.deletePurchase(id);
            if (deleted) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Purchase deleted successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Purchase not found");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete purchase: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/options/districts")
    public ResponseEntity<?> getValidDistricts() {
        try {
            List<String> districts = purchaseService.getValidDistricts();
            Map<String, Object> response = new HashMap<>();
            response.put("districts", districts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve districts: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/options/products")
    public ResponseEntity<?> getValidProducts() {
        try {
            List<String> products = purchaseService.getValidProducts();
            Map<String, Object> response = new HashMap<>();
            response.put("products", products);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve products: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
