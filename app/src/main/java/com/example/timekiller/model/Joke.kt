package com.example.timekiller.model

class Joke(
    private val id: Int,
    private val type: String,
    private val setup: String,
    private val punchline: String
){
    fun getType() : String = this.type
    fun getSetup(): String = this.setup
    fun getPunchline(): String = this.punchline
}