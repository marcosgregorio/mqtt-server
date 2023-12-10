package com.app;

import java.util.ArrayList;

public class Group {
    private String groupName;
    private String administrator;
    private ArrayList<Contact> contacts = new ArrayList<>();

    public Group(String groupName, String administrator, ArrayList<Contact> contacts) {
        this.groupName = groupName;
        this.administrator = administrator;
        this.contacts = contacts;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAdministrator() {
        return administrator;
    }

    public void setAdministrator(String administrator) {
        this.administrator = administrator;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }
    
    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }
}
