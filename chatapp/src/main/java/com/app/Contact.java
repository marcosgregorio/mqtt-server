package com.app;

final public class Contact {
    private User user;

    public Contact(User user) {
        this.user = user;
    }

    public String getName() {
        return this.user.name;
    }

    public void setName(String name) {
        this.user.name = name;
    }

    public boolean getStatus() {
        return this.user.status;
    }

    public void setStatus(boolean status) {
        this.user.status = status;
    }
}
