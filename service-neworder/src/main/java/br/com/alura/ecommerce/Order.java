package br.com.alura.ecommerce;

import java.math.BigDecimal;

public class Order {
    private final String orderId, userEmail;
    private final BigDecimal amount;

//    public Order(String userID, String orderId, BigDecimal amount, String userEmail) {
//        this.userID = userID;
//        this.orderId = orderId;
//        this.amount = amount;
//        this.userEmail = userEmail;
//    }

    public Order(String orderId, BigDecimal amount, String userEmail) {
        this.orderId = orderId;
        this.amount = amount;
        this.userEmail = userEmail;
    }

}
