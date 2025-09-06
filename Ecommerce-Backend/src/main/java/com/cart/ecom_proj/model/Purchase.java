package com.cart.ecom_proj.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Username is required")
    private String username;

    @Column(nullable = false)
    @NotNull(message = "Purchase date is required")
    @Future(message = "Purchase date must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    @Column(nullable = false)
    @NotBlank(message = "Delivery time is required")
    @Pattern(regexp = "^(10 AM|11 AM|12 PM)$", message = "Delivery time must be 10 AM, 11 AM, or 12 PM")
    private String deliveryTime;

    @Column(nullable = false)
    @NotBlank(message = "Delivery location is required")
    private String deliveryLocation;

    @Column(nullable = false)
    @NotBlank(message = "Product name is required")
    private String productName;

    @Column(nullable = false)
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 100, message = "Quantity cannot exceed 100")
    private Integer quantity;

    @Column(columnDefinition = "TEXT")
    @Size(max = 500, message = "Message cannot exceed 500 characters")
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Purchase() {}

    public Purchase(String username, LocalDate purchaseDate, String deliveryTime, 
                   String deliveryLocation, String productName, Integer quantity, String message) {
        this.username = username;
        this.purchaseDate = purchaseDate;
        this.deliveryTime = deliveryTime;
        this.deliveryLocation = deliveryLocation;
        this.productName = productName;
        this.quantity = quantity;
        this.message = message;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", deliveryTime='" + deliveryTime + '\'' +
                ", deliveryLocation='" + deliveryLocation + '\'' +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
