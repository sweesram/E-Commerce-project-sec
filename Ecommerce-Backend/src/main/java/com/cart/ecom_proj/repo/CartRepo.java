package com.cart.ecom_proj.repo;

import com.cart.ecom_proj.model.Cart;
import com.cart.ecom_proj.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);

    List<Cart> findByUserId(Long userId);

    @Query("SELECT c FROM Cart c WHERE c.user = :user AND c.product.id = :productId")
    Optional<Cart> findByUserAndProductId(@Param("user") User user, @Param("productId") Integer productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.user = :user AND c.product.id = :productId")
    void deleteByUserAndProductId(@Param("user") User user, @Param("productId") Integer productId);

    @Modifying
    @Transactional
    void deleteByUser(User user);

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);
}
