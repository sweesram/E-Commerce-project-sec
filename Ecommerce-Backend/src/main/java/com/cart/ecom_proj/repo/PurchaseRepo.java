package com.cart.ecom_proj.repo;

import com.cart.ecom_proj.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseRepo extends JpaRepository<Purchase, Long> {
    
    // Find all purchases by username
    List<Purchase> findByUsernameOrderByPurchaseDateDesc(String username);
    
    // Find purchases by username and date range
    @Query("SELECT p FROM Purchase p WHERE p.username = :username AND p.purchaseDate BETWEEN :startDate AND :endDate ORDER BY p.purchaseDate DESC")
    List<Purchase> findByUsernameAndDateRange(@Param("username") String username, 
                                            @Param("startDate") LocalDate startDate, 
                                            @Param("endDate") LocalDate endDate);
    
    // Find upcoming deliveries (future dates)
    @Query("SELECT p FROM Purchase p WHERE p.username = :username AND p.purchaseDate >= :currentDate ORDER BY p.purchaseDate ASC")
    List<Purchase> findUpcomingDeliveries(@Param("username") String username, @Param("currentDate") LocalDate currentDate);
    
    // Find past deliveries
    @Query("SELECT p FROM Purchase p WHERE p.username = :username AND p.purchaseDate < :currentDate ORDER BY p.purchaseDate DESC")
    List<Purchase> findPastDeliveries(@Param("username") String username, @Param("currentDate") LocalDate currentDate);
    
    // Check if purchase date is not Sunday
    @Query("SELECT p FROM Purchase p WHERE p.username = :username AND DAYOFWEEK(p.purchaseDate) != 1 ORDER BY p.purchaseDate DESC")
    List<Purchase> findByUsernameExcludingSundays(@Param("username") String username);
}
