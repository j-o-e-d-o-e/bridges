package net.joedoe.utils;

import java.time.LocalTime;

public class TimeEntry {
    private LocalTime time;
    private String name;

    public TimeEntry(LocalTime time, String name) {
        this.time = time;
        this.name = name;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
