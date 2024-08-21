package br.com.alura.ecommerce;

import br.com.alura.ecommerce.consumer.KafkaService;
import br.com.alura.ecommerce.dispatcher.KafkaDispatcher;
import br.com.alura.ecommerce.services.ConsumerService;
import br.com.alura.ecommerce.services.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.concurrent.ExecutionException;


public class EmailNewOrderService implements ConsumerService<Order> {

    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new ServiceRunner(EmailNewOrderService::new).start(1);

    }

    @Override
    public String getConsumerGroup() {
        return EmailNewOrderService.class.getSimpleName();
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    public void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {

        var message = record.value();
        var emailCode = "Thank you for your order! We are processing it!";
        var order = record.value().getPayload();
        var id = message.getId().continueWith(EmailNewOrderService.class.getSimpleName());

        var messageToSend = new StringBuilder();
        messageToSend.append("---------------------------------------------------------\n")
                .append("--------- Processing new order. Preparing email ---------\n")
                .append(message)
                .append("\n---------------------------------------------------------");

        System.out.println(messageToSend);

        emailDispatcher.send("ECOMMERCE_SEND_EMAIL",
                order.getEmail(),
                id,
                emailCode);
    }
}
