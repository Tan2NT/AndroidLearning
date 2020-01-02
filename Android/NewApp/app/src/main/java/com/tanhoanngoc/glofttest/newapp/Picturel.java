package com.tanhoanngoc.glofttest.newapp;

public class Picturel {
    private String name;
    private int picID;

    public Picturel(String name, int picID) {
        this.name = name;
        this.picID = picID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPicID() {
        return picID;
    }

    public void setPicID(int picID) {
        this.picID = picID;
    }
}
