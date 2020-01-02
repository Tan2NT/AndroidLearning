package com.tanhoanngoc.glofttest.newapp;

public class Fruit {
    private String name;
    private String description;
    private int igmID;

    public Fruit(String name, String description, int igmID) {
        this.name = name;
        this.description = description;
        this.igmID = igmID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIgmID() {
        return igmID;
    }

    public void setIgmID(int igmID) {
        this.igmID = igmID;
    }
}
