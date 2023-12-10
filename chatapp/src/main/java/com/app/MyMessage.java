package com.app;

import java.util.ArrayList;

public class MyMessage {

    public String message;
    public String type;
    public String id;
    public String topic;
    public int groupIndex;
    public ArrayList<Group> groups;
    
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

    MyMessage(String topic, String message, String type, String id, int groupIndex, ArrayList<Group> groups) {
        this.topic = topic;
        this.message = message;
        this.type = type;
        this.id = id;
        this.groupIndex = groupIndex;
        this.groups = groups;
    }

}
