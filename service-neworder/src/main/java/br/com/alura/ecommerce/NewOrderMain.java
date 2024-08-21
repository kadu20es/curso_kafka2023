package br.com.alura.ecommerce;

import br.com.alura.ecommerce.dispatcher.KafkaDispatcher;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var orderDispatcher = new KafkaDispatcher<>()) { // se der sucesso ou sair por causa de uma exception, o dispatcher será fechado

        //var producer = new KafkaProducer<String, String>(properties()); // <chave, valor> --- cria o produtor
            var userEmail = Math.random() + "@mail.com";
            for (int i =0; i < 10; i++) {

                var orderId = UUID.randomUUID().toString();
                var orderAmount = new BigDecimal(Math.random() * 5000 + 1.99);

                var id = new CorrelationId(NewOrderMain.class.getSimpleName());

                var order = new Order(orderId, orderAmount, userEmail);
                orderDispatcher.send("ECOMMERCE_NEW_ORDER",
                        userEmail,
                        id,
                        order);
            }
        }
    }
}
