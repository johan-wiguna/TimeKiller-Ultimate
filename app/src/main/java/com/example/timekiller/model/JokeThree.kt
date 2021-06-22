package com.example.timekiller.model

class JokeThree(
    private val id: Int,
    private val joke: String,
    private val categories: Array<String>
) {
    fun getJoke(): String = this.joke
}