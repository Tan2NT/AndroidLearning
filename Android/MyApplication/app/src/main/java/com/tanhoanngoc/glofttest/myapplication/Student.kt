package com.tanhoanngoc.glofttest.myapplication

import android.util.Log

class Student(name: String?, address: String?, yearOfBird: Int){
    val TAG : String = "TDebug";

    private var name : String? = null
    private var address : String? = null
    private var yearOfBird: Int = 0

    init {
        this.name = name
        this.address = address
        this.yearOfBird = yearOfBird
    }

//    constructor(name: String?, address: String?, yearOfBird: Int) {
//        this.name = name
//        this.address = address
//        this.yearOfBird = yearOfBird
//    }

    public fun ShowInfo(){
        Log.i(TAG, "Hello " + name + " from " + address + " since " + yearOfBird.toString())
    }

}