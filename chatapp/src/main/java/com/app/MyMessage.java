package com.app;

public class MyMessage {

    public String message;
    public String type;
    public String id;
    public String topic;
    
    MyMessage(String message, String type, String id) {
        this.message = message;
        this.type = type;
        this.id = id;
    }

    MyMessage(String topic, String message, String type, String id) {
        this.topic = topic;
        this.message = message;
        this.type = type;
        this.id = id;
    }
}
