package com.backend.amqp;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * A temporary implementation of CommandLineRunner for output visualisation
 */
@Component
public class RabbitMqRunner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public RabbitMqRunner(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(
                MessagingConfig.topicExchangeName, "players.pending.default", "Message test");
        receiver.getLatch().await(5000, TimeUnit.MILLISECONDS);
    }

}
