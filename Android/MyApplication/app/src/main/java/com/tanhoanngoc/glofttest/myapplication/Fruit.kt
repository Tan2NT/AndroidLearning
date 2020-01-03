package com.tanhoanngoc.glofttest.myapplication

class Fruit {

    private var name : String = ""
    private var description : String = ""
    private var pictureID : Int = -1

    constructor(name: String, description: String, pictureID: Int) {
        this.name = name
        this.description = description
        this.pictureID = pictureID
    }

    public fun getName() : String{
        return name;
    }

    public fun getDescription() : String {
        return description;
    }

    public fun getPictureId() : Int {
        return pictureID;
    }
}