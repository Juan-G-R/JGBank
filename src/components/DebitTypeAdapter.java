package components;

import java.lang.reflect.Type;
import java.time.LocalDate;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DebitTypeAdapter implements JsonDeserializer<Debit> {

    @Override
    public Debit deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String comment = jsonObject.get("comment").getAsString();
        int identifier = jsonObject.get("identifier").getAsInt();
        double balance = jsonObject.get("balance").getAsDouble();
        int targetAccountNumber = jsonObject.get("targetAccountNumber").getAsInt();
        boolean effect = jsonObject.get("effect").getAsBoolean();
        LocalDate dateOfFlow = LocalDate.parse(jsonObject.get("dateOfFlow").getAsString());

        return new Debit(comment, identifier, balance, targetAccountNumber, effect, dateOfFlow);
    }
}