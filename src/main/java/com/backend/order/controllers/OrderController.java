package com.backend.order.controllers;

import com.backend.order.payload.request.CreateOrderRequest;
import com.backend.order.service.OrderService;
import com.backend.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final static Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
        log.info("OrderController initialized");
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest orderRequest) {
        log.info("Received orderRequest {}", orderRequest);
        Order order = orderService.createOrder(orderRequest);
        return ResponseEntity.ok("Order created successfully");
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        log.info("Received request to get order with ID {}", orderId);
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
}