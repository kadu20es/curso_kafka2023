package br.com.alura.ecommerce;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Order {
    private final String orderId, userEmail;
    private final BigDecimal amount;

    public Order(String orderId, BigDecimal amount, String userEmail) {
        this.orderId = orderId;
        this.amount = amount;
        this.userEmail = userEmail;
    }



    @Override
    public String toString() {
        NumberFormat formatter = new DecimalFormat("0000.00");

        return "Order{" +
                ", orderId='" + orderId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", amount=" + formatter.format(amount) +
                '}';
    }


    String getEmail() { return this.userEmail; }

    BigDecimal getAmount() {
        return amount;
    }

    public String getOrderId(){ return orderId;  }
}
