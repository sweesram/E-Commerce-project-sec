package com.cart.ecom_proj.service;

import com.cart.ecom_proj.model.Cart;
import com.cart.ecom_proj.model.Product;
import com.cart.ecom_proj.model.User;
import com.cart.ecom_proj.repo.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    
    @Autowired
    private CartRepo cartRepo;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;
    
    public List<Cart> getUserCart(Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return cartRepo.findByUser(user);
    }
    
    public Cart addToCart(Long userId, Integer productId, Integer quantity) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        
        if (user == null || product == null) {
            throw new RuntimeException("User or product not found");
        }
        
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        Optional<Cart> existingCart = cartRepo.findByUserAndProductId(user, productId);
        
        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            cart.setUpdatedAt(LocalDateTime.now());
            return cartRepo.save(cart);
        } else {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setProduct(product);
            newCart.setQuantity(quantity);
            newCart.setPrice(product.getPrice());
            return cartRepo.save(newCart);
        }
    }
    
    public void removeFromCart(Long userId, Integer productId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        cartRepo.deleteByUserAndProductId(user, productId);
    }
    
    public void updateCartQuantity(Long userId, Integer productId, Integer quantity) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        
        if (user == null || product == null) {
            throw new RuntimeException("User or product not found");
        }
        
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        Optional<Cart> cart = cartRepo.findByUserAndProductId(user, productId);
        if (cart.isPresent()) {
            Cart cartItem = cart.get();
            cartItem.setQuantity(quantity);
            cartItem.setUpdatedAt(LocalDateTime.now());
            cartRepo.save(cartItem);
        }
    }
    
    public void clearCart(Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        cartRepo.deleteByUser(user);
    }
    
    public BigDecimal getCartTotal(Long userId) {
        List<Cart> cartItems = getUserCart(userId);
        return cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
