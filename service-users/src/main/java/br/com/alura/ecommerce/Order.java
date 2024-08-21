package br.com.alura.ecommerce;

import java.math.BigDecimal;

public class Order {
    private final String orderId, userEmail;
    private final BigDecimal amount;

    public Order(String orderId, BigDecimal amount, String userEmail) {
        this.orderId = orderId;
        this.amount = amount;
        this.userEmail = userEmail;
    }

    String getEmail() {
        return this.userEmail;
    }

    @Override
    public String toString() {

        return "Order{" +
                ", email=" + userEmail +
                '}';
    }


}
