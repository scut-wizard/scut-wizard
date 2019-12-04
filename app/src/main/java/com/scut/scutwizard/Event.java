package com.scut.scutwizard;

public class Event {
    public String name;
    public double progress;
    public boolean isfinish;

    public Event(String tname) {
        name = tname;
        progress = 50;
        isfinish = false;
    }

    public void setName(String name) {
        this.name = name;
    }
}
