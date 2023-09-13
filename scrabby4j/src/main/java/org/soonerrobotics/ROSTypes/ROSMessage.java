package org.soonerrobotics.ROSTypes;

import com.google.gson.JsonElement;

public class ROSMessage {
    public String op;
    public String topic;
    public String type;

    public JsonElement data;
    public JsonElement msg;

    public String toString() {
        return "op: " + op + ", topic: " + topic + ", type: " + type + ", data: " + data;
    }
}
