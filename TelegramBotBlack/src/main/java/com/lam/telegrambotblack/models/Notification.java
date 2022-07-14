package com.lam.telegrambotblack.models;

import com.fasterxml.jackson.databind.JsonNode;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class Notification {

    JsonObject jsonObject;

    public Notification(JsonNode jsonNode) {
        JsonReader reader = Json.createReader(new StringReader(jsonNode.toString()));
        jsonObject = reader.readObject();
    }

    public String getType(){
        return jsonObject.getString("type_notification");
    }

    public String getText() {
        return jsonObject.getString("text_notification");
    }
}
