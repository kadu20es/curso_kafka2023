package br.com.alura.ecommerce.services;

public interface ServiceFactory<T> {
    ConsumerService<T> create() throws Exception;
}
