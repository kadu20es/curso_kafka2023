package br.com.alura.ecommerce;

import br.com.alura.ecommerce.database.LocalDatabase;
import br.com.alura.ecommerce.dispatcher.KafkaDispatcher;
import br.com.alura.ecommerce.services.ConsumerService;
import br.com.alura.ecommerce.services.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;


import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;


public class FraudDetectorService implements ConsumerService<Order> {
    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
    private final LocalDatabase database;

    FraudDetectorService() throws SQLException {
        this.database = new LocalDatabase("frauds_database");
        this.database.createIfNotExists("create table Orders(" +
            "uuid varchar(200) primary key" +
            "is_fraud boolean)");
    }
    public static void main(String[] args) {
        new ServiceRunner(FraudDetectorService::new).start(3);
    }

    @Override
    public String getConsumerGroup() {
        return FraudDetectorService.class.getSimpleName();
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    public void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException, SQLException {

        System.out.println("---------------------------------------------------------\n" +
        "Processing new order. Checking for fraud");
        var message = record.value();
        System.out.println("Sucesso ao consumir de " + record.topic()
                + " ::: [partition: " + record.partition() + "]"
                + "[offset: " + record.offset() + "]"
                + "[key: " + record.key() + "]"
                + "[value: " + record.value() + "]"
                + "[timestamp: " + record.timestamp() + "]");
        var order = message.getPayload();
        if(wasProcessed(order)){
            System.out.println("Order " + order + " was already processed");
            return;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // a partir daqui, o serviço deixa de ser apenas um consumidor e passa a ser também um produtor
        if (isaFraud(order)){
            // pretending that the fraud happens when the amount é >= 4500
            System.out.println("Order is a fraud");
            database.update("insert into Orders (uuid, is_fraud values (?,true)", order.getOrderId());
            orderDispatcher.send("ECOMMERCE_ORDER_REJECTED", order.getEmail(), message.getId().continueWith(FraudDetectorService.class.getSimpleName()), order);
        } else {
            System.out.println("Order " + order + " approved.");
            database.update("insert into Orders (uuid, is_fraud values (?,false)", order.getOrderId());
            orderDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getEmail(), message.getId().continueWith(FraudDetectorService.class.getSimpleName()), order);
        }
    }

    private boolean wasProcessed(Order order) throws SQLException {
        var results = database.query("select uuid from Order where uuid = ? limit 1", order.getOrderId());
        return results.next();
    }

    private static boolean isaFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
    }

}
