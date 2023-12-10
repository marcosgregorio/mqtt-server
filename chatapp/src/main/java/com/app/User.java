package com.app;

public class User {
    public String name;
    public boolean status;

    User(String name, boolean status) {
        this.name = name;
        this.status = status;
    }

    public String getNome() {
        return name;
    }

    public void setNome(String name) {
        this.name = name;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
