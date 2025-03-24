package com.backend.order.controller;

import com.backend.order.service.OrderService;
import com.backend.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
        log.info("OrderController initialized");
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        log.info("Received order {}", order);
        orderService.createOrder(order);
        return ResponseEntity.ok("Order created successfully");
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
}