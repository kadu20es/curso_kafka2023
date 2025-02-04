package br.com.alura.ecommerce.dispatcher;

import br.com.alura.ecommerce.CorrelationId;
import br.com.alura.ecommerce.Message;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaDispatcher<T> implements Closeable {

    private final KafkaProducer<String, Message<T>> producer;
    public KafkaDispatcher(){
        this.producer = new KafkaProducer<>(properties());
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9090"); // configura o local de acesso ao kafka
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // converte a classe de string para bytes
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName()); // converte as mensagens string para bytes
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        return properties;
    }

    /**
     * Envia a mensagem
     * @param topic
     * @param key
     * @param payload
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void send(String topic, String key, CorrelationId id, T payload) throws ExecutionException, InterruptedException {
        Future<RecordMetadata> future = sendAsync(topic, key, id, payload);
        future.get();
    }

    public Future<RecordMetadata> sendAsync(String topic, String key, CorrelationId id, T payload) {
        var value = new Message<>(id.continueWith("_" + topic), payload);
        var record = new ProducerRecord<>(topic, key, value); // <chave, valor>(TOPICO, CHAVE, VALOR)

        Callback callback = (data, ex) -> { // o (data, ex) -> { é uma callback function que se tiver o erro, imprime o erro, se não tiver, imprime os dados do envio
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            System.out.println("sucesso ao enviar " + data.topic() + " ::: [partition: " + data.partition() + "] [offset: " + data.offset() + "][timestamp: " + data.timestamp() + "]");
        };
        return producer.send(record, callback);
    }

    @Override
    public void close() {
        //producer.close();
    }
}
