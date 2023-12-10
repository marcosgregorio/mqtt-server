package com.app;

public class Session {

    private String sessionName;
    private String sendersId;
    private int groupIndex;

    public Session(String sessionName, String sendersId, int groupIndex) {
        this.setSessionName(sessionName);
        this.setSendersId(sendersId);
        this.setGroupIndex(groupIndex);
    }

    public Session(String sessionName, String sendersId) {
        this.setSessionName(sessionName);
        this.setSendersId(sendersId);
    }
    
    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSendersId() {
        return sendersId;
    }
    
    public void setSendersId(String sendersId) {
        this.sendersId = sendersId;
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }
}
