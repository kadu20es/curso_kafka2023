package br.com.alura.ecommerce;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Cria a "casca" do objeto que será serializado, contendo o ID e o Payload (mensagem) - SERIALIZADOR
 */
public class MessageAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {

    @Override
    public JsonElement serialize(Message message, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", message.getPayload().getClass().getName()); // pega o nome do classe do objeto que será serializado
        obj.add("payload", context.serialize(message.getPayload()));
        obj.add("correlationId", context.serialize(message.getId()));
        return obj;
    }

    @Override
    public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        var obj = jsonElement.getAsJsonObject();
        var payloadType = obj.get("type").getAsString(); // nome da classe
        var correlationId = (CorrelationId) context.deserialize(obj.get("correlationId"), CorrelationId.class); // .class indica o tipo do objeto

        try {
            // talvez você queira criar uma lista de classes que possam ser desserializadas
            var payload = context.deserialize(obj.get("payload"), Class.forName(payloadType));
            return new Message(correlationId, payload);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}
