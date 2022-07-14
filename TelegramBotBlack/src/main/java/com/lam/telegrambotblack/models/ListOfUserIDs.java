package com.lam.telegrambotblack.models;

import com.fasterxml.jackson.databind.JsonNode;

import javax.json.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ListOfUserIDs {
    JsonObject jsonObject;

    public ListOfUserIDs(JsonNode jsonNode) {
        JsonReader reader = Json.createReader(new StringReader(jsonNode.toString()));
        jsonObject = reader.readObject();
    }

    public List<Long> getTelegramChatIDs() {
        List<Long> listTelegramID = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getJsonArray("telegram_chat_id_list");

        for (JsonValue id: jsonArray) {
            listTelegramID.add(Long.parseLong(id.toString()));
        }

        return listTelegramID;
    }
}
