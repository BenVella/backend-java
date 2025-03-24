package com.backend.order.service;

import com.backend.order.messaging.MessagingConfig;
import com.backend.order.messaging.Receiver;
import com.backend.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public OrderService(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
        log.info("OrderService initialized");
    }

    public void pushMessage(Object message) throws InterruptedException {
        rabbitTemplate.convertAndSend(
                MessagingConfig.topicExchangeName, "orders.pending.default",
                message);

        boolean completed = receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        if (!completed) {
            throw new IllegalStateException("Latch await exceeded timeout");
        }
    }

    public void createOrder(Order order) {
        try {
            log.info("Sending order to queue {}", order);
            pushMessage(order);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Validate order details
        // Logic to create order
        // Publish event to RabbitMQ or Kafka
        // Logic to validate and create order
        // Publish event to RabbitMQ or Kafka
    }

    public Order getOrderById(Long orderId) {
        // Logic to retrieve order by ID
        return new Order(); // Replace with actual retrieval logic
    }
}