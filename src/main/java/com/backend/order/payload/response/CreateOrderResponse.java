package com.backend.order.payload.response;

import com.backend.order.model.Order;

public class CreateOrderResponse {
  private final Order orderDetails;

    public CreateOrderResponse(Order orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Order getOrderDetails() {
        return orderDetails;
    }
}
