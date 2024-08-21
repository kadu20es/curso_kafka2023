package br.com.alura.ecommerce;

import java.math.BigDecimal;

public class Order {
    private final String userID, orderId, email;
    private final BigDecimal amount;

    public Order(String userID, String orderId, BigDecimal amount, String email) {
        this.userID = userID;
        this.orderId = orderId;
        this.amount = amount;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Order{" +
                "userID='" + userID + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
