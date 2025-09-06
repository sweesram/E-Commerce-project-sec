package com.cart.ecom_proj.service;

import com.cart.ecom_proj.model.Purchase;
import com.cart.ecom_proj.repo.PurchaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepo purchaseRepo;

    // Predefined list of districts for delivery locations
    private static final List<String> VALID_DISTRICTS = List.of(
        "Colombo", "Gampaha", "Kalutara", "Kandy", "Matale", "Nuwara Eliya",
        "Galle", "Matara", "Hambantota", "Jaffna", "Vanni", "Batticaloa",
        "Digamadulla", "Trincomalee", "Kurunegala", "Puttalam", "Anuradhapura",
        "Polonnaruwa", "Badulla", "Moneragala", "Ratnapura", "Kegalle"
    );

    // Predefined list of products
    private static final List<String> VALID_PRODUCTS = List.of(
        "Laptop", "Smartphone", "Tablet", "Desktop Computer", "Monitor",
        "Keyboard", "Mouse", "Headphones", "Speaker", "Camera",
        "Printer", "Router", "External Hard Drive", "USB Cable", "Power Bank"
    );

    public Purchase createPurchase(Purchase purchase) {
        // Validate purchase date (not Sunday and not in the past)
        validatePurchaseDate(purchase.getPurchaseDate());
        
        // Validate delivery time
        validateDeliveryTime(purchase.getDeliveryTime());
        
        // Validate delivery location
        validateDeliveryLocation(purchase.getDeliveryLocation());
        
        // Validate product name
        validateProductName(purchase.getProductName());
        
        return purchaseRepo.save(purchase);
    }

    public List<Purchase> getAllPurchasesByUsername(String username) {
        return purchaseRepo.findByUsernameOrderByPurchaseDateDesc(username);
    }

    public List<Purchase> getUpcomingDeliveries(String username) {
        return purchaseRepo.findUpcomingDeliveries(username, LocalDate.now());
    }

    public List<Purchase> getPastDeliveries(String username) {
        return purchaseRepo.findPastDeliveries(username, LocalDate.now());
    }

    public Optional<Purchase> getPurchaseById(Long id) {
        return purchaseRepo.findById(id);
    }

    public Purchase updatePurchase(Long id, Purchase purchaseDetails) {
        Optional<Purchase> optionalPurchase = purchaseRepo.findById(id);
        if (optionalPurchase.isPresent()) {
            Purchase purchase = optionalPurchase.get();
            
            // Validate updated fields
            if (purchaseDetails.getPurchaseDate() != null) {
                validatePurchaseDate(purchaseDetails.getPurchaseDate());
                purchase.setPurchaseDate(purchaseDetails.getPurchaseDate());
            }
            
            if (purchaseDetails.getDeliveryTime() != null) {
                validateDeliveryTime(purchaseDetails.getDeliveryTime());
                purchase.setDeliveryTime(purchaseDetails.getDeliveryTime());
            }
            
            if (purchaseDetails.getDeliveryLocation() != null) {
                validateDeliveryLocation(purchaseDetails.getDeliveryLocation());
                purchase.setDeliveryLocation(purchaseDetails.getDeliveryLocation());
            }
            
            if (purchaseDetails.getProductName() != null) {
                validateProductName(purchaseDetails.getProductName());
                purchase.setProductName(purchaseDetails.getProductName());
            }
            
            if (purchaseDetails.getQuantity() != null) {
                purchase.setQuantity(purchaseDetails.getQuantity());
            }
            
            if (purchaseDetails.getMessage() != null) {
                purchase.setMessage(purchaseDetails.getMessage());
            }
            
            return purchaseRepo.save(purchase);
        }
        return null;
    }

    public boolean deletePurchase(Long id) {
        if (purchaseRepo.existsById(id)) {
            purchaseRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean isPurchaseOwnedByUser(Long purchaseId, String username) {
        Optional<Purchase> purchase = purchaseRepo.findById(purchaseId);
        return purchase.isPresent() && purchase.get().getUsername().equals(username);
    }

    private void validatePurchaseDate(LocalDate purchaseDate) {
        if (purchaseDate == null) {
            throw new IllegalArgumentException("Purchase date cannot be null");
        }
        
        if (purchaseDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Purchase date cannot be in the past");
        }
        
        if (purchaseDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Purchase date cannot be on Sunday");
        }
    }

    private void validateDeliveryTime(String deliveryTime) {
        if (deliveryTime == null || deliveryTime.trim().isEmpty()) {
            throw new IllegalArgumentException("Delivery time is required");
        }
        
        if (!deliveryTime.equals("10 AM") && !deliveryTime.equals("11 AM") && !deliveryTime.equals("12 PM")) {
            throw new IllegalArgumentException("Delivery time must be 10 AM, 11 AM, or 12 PM");
        }
    }

    private void validateDeliveryLocation(String deliveryLocation) {
        if (deliveryLocation == null || deliveryLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("Delivery location is required");
        }
        
        if (!VALID_DISTRICTS.contains(deliveryLocation)) {
            throw new IllegalArgumentException("Invalid delivery location. Must be one of: " + String.join(", ", VALID_DISTRICTS));
        }
    }

    private void validateProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        
        if (!VALID_PRODUCTS.contains(productName)) {
            throw new IllegalArgumentException("Invalid product name. Must be one of: " + String.join(", ", VALID_PRODUCTS));
        }
    }

    public List<String> getValidDistricts() {
        return VALID_DISTRICTS;
    }

    public List<String> getValidProducts() {
        return VALID_PRODUCTS;
    }
}
