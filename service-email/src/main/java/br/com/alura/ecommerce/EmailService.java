package br.com.alura.ecommerce;

import br.com.alura.ecommerce.services.ConsumerService;
import br.com.alura.ecommerce.services.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.ExecutionException;

public class EmailService implements ConsumerService<String> {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new ServiceRunner(EmailService::new).start(5);  // executa a thread
    }

    public String getConsumerGroup(){
        return EmailService.class.getSimpleName();
    }
    public String getTopic(){
        return "ECOMMERCE_SEND_EMAIL";
    }

    public void parse(ConsumerRecord<String, Message<String>> record) {

        System.out.println("Sending e-mail");
        System.out.println("Sucesso ao consumir de "
                + record.topic()
                + " ::: [partition: " + record.partition() + "]"
                + "[offset: " + record.offset() + "]"
                + "[key: " + record.key() + "]"
                + "[value: " + record.value() + "]"
                + "[timestamp: " + record.timestamp() + "]");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("E-mail sent");
    }

}
