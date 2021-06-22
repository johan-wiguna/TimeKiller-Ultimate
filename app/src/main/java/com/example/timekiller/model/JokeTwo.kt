package com.example.timekiller.model

class JokeTwo(
    private val id: Int,
    private val joke: String,
    private val status: Int
) {
    fun getJoke(): String = this.joke
}