package com.cart.ecom_proj.service;

import com.cart.ecom_proj.model.*;
import com.cart.ecom_proj.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Transactional
    public Order createOrder(Long userId, Map<String, String> orderDetails) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> cartItems = cartRepo.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Calculate total amount first
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart cartItem : cartItems) {
            totalAmount = totalAmount.add(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(orderDetails.get("shippingAddress"));
        order.setPaymentMethod(orderDetails.get("paymentMethod"));
        order.setPhoneNumber(orderDetails.get("phoneNumber"));
        order.setTotalAmount(totalAmount);
        order.setStatus(Order.OrderStatus.CONFIRMED);

        // Save order first to get ID
        order = orderRepo.save(order);

        // Create order items from cart
        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();

            // Check stock availability
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setTotalPrice(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            orderItemRepo.save(orderItem);

            // Update product stock
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            if (product.getStockQuantity() == 0) {
                product.setProductAvailable(false);
            }
            productRepo.save(product);
        }

        // Clear user's cart
        cartRepo.deleteByUser(user);

        return order;
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepo.findByUserIdOrderByOrderDateDesc(userId);
    }

    public Order getOrderById(Long orderId) {
        return orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = getOrderById(orderId);
        order.setStatus(Order.OrderStatus.valueOf(status.toUpperCase()));
        return orderRepo.save(order);
    }
}
