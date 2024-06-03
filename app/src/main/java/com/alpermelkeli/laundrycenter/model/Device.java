package com.alpermelkeli.laundrycenter.model;

public class Device {
    private String name;



    private String id;
    private String channel;
    private long start;
    private long time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
