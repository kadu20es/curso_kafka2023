package br.com.alura.ecommerce;

import java.math.BigDecimal;

public class Order {
    private final String userEmail, orderId;
    private final BigDecimal amount;

    public Order(String userEmail, String orderId, BigDecimal amount) {
        this.userEmail = userEmail;
        this.orderId = orderId;
        this.amount = amount;
    }
}
