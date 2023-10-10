package com.example.sqliteapp

class CustomerModel {
    private var id: Int? = null
    private var name: String? = null
    private var age: Int? = null
    private var isActive: Boolean? = null

    constructor(id: Int, name: String, age: Int, isActive: Boolean){
        this.id = id
        this.name = name
        this.age = age
        this.isActive = isActive
    }

    constructor(){
    }

    override fun toString(): String {
        return "CustomerModel(id=$id, name=$name, age=$age, isActive=$isActive)"
    }

    fun getId(): Int?{
        return this.id
    }
    fun getName(): String?{
        return this.name
    }
    fun getAge(): Int?{
        return this.age
    }
    fun getActive(): Boolean?{
        return this.isActive
    }
}