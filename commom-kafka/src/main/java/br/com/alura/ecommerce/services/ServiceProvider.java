package br.com.alura.ecommerce.services;

import br.com.alura.ecommerce.consumer.KafkaService;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ServiceProvider<T> implements Callable<Void> {

    private final ServiceFactory<T> factory;
    public ServiceProvider(ServiceFactory<T> factory) {
        this.factory = factory;
    }

    /**
     * Cria uma fábrica de threads para serem usadas nos serviços dentro dos métodos main()
     * @param factory
     * @param <T>
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Void call() throws Exception {
        var myService = factory.create();

        try (var service = new KafkaService(
                myService.getConsumerGroup(),
                myService.getTopic(),
                myService::parse,
                Map.of())) {
            service.run();
        }

        return null;
    }

}
