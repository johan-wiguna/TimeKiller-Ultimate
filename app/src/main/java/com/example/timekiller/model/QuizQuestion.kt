package com.example.timekiller.model

class QuizQuestion(
    private val category: String,
    private val type: String,
    private val difficulty: String,
    private val question: String,
    private val correct_answer: String,
    private val incorrect_answers: ArrayList<String>
) {
    fun getCategory(): String = this.category
    fun getType(): String = this.type
    fun getDifficulty(): String = this.difficulty
    fun getQuestion(): String = this.question
    fun getCorrectAnswer(): String = this.correct_answer
    fun getIncorrectAnswer(): ArrayList<String> = this.incorrect_answers
}