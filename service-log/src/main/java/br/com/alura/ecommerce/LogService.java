package br.com.alura.ecommerce;

import br.com.alura.ecommerce.consumer.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class LogService {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var logService = new LogService();
        try (var service = new KafkaService(
                LogService.class.getSimpleName(),
                Pattern.compile("ECOMMERCE.*"), // Se inscreve em TODOS os tópicos
                logService::parse,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()))) { // passa propriedades extras informando que o value_deserializer é um stringDeserializer
            service.run();
            }
        }

    private void parse(ConsumerRecord<String, Message<String>> record) {
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("Sucesso ao consumir de " + record.topic() + " ::: [partition: " + record.partition() + "]" +
                "[offset: " + record.offset() + "][key: " + record.key() + "][value: " + record.value() + "]" +
                "[timestamp: " + record.timestamp() + "]");
    }
}
