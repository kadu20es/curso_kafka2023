package br.com.alura.ecommerce.dispatcher;

import br.com.alura.ecommerce.Message;
import br.com.alura.ecommerce.MessageAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;


/**
 * classe para serialização em json
 * @param <T>
 */
public class GsonSerializer<T> implements Serializer<T> {

    //private final Gson gson = new GsonBuilder().create();
    /**
     * cria um adapter no serializador para usar um tipo customizado - o tipo "message". Vai utilizr o CorrelationID, Message e MessageAdapter
     */
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapter()).create(); //

    @Override
    public byte[] serialize(String s, T object) {
        return gson.toJson(object).getBytes();
    }
}
