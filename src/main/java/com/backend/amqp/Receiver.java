package com.backend.amqp;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(byte[] message) {
        // Convert byte[] to String and delegate to the existing method
        String decodedMessage = new String(message, StandardCharsets.UTF_8);
        receiveMessage(decodedMessage);
    }

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}
