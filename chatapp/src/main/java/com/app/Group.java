package com.app;

import java.util.ArrayList;

public class Group {
    private String groupName;
    private Messenger administrator;
    private ArrayList<Messenger> messengers = new ArrayList<>();

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Messenger getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Messenger administrator) {
        this.administrator = administrator;
    }

    public ArrayList<Messenger> getMessengers() {
        return messengers;
    }
    
    public void setMessengers(ArrayList<Messenger> messengers) {
        this.messengers = messengers;
    }
}
