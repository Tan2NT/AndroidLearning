package com.example.intent

import java.io.Serializable

class Student : Serializable {
    var name : String = ""
    var age : Int = 0

    constructor(name: String, age: Int) {
        this.name = name
        this.age = age
    }
}