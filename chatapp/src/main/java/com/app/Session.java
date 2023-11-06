package com.app;

public class Session {

    private String sessionName;
    private String sendersId;
    
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
}
