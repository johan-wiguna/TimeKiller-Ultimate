package com.example.timekiller.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timekiller.model.QuizQuestion
import com.example.timekiller.repository.QuizRepository

class QuizViewModel: ViewModel() {
    val repository = QuizRepository()

    //Score
    private val score = MutableLiveData<Int>()

    fun getScore(): LiveData<Int> = this.score

    fun setScore(score: Int) {
        this.score.value = score
    }

    fun addScore(amount: Int) {
        this.score.value = this.score.value!! + amount
    }

    fun subScore(amount: Int) {
        this.score.value = this.score.value!! - amount
    }

    //Easy High Score
    private val easyHighScore = MutableLiveData<Int>()

    fun getEasyHighScore(): LiveData<Int> = this.easyHighScore

    fun setEasyHighScore(newHighScore: Int) {
        this.easyHighScore.value = newHighScore
    }

    //Medium High Score
    private val mediumHighScore = MutableLiveData<Int>()

    fun getMediumHighScore(): LiveData<Int> = this.mediumHighScore

    fun setMediumHighScore(newHighScore: Int) {
        this.mediumHighScore.value = newHighScore
    }

    //Hard High Score
    private val hardHighScore = MutableLiveData<Int>()

    fun getHardHighScore(): LiveData<Int> = this.hardHighScore

    fun setHardHighScore(newHighScore: Int) {
        this.hardHighScore.value = newHighScore
    }

    //Current Question
    private val currQuizQuestion = MutableLiveData<ArrayList<QuizQuestion>>()

    fun getCurrQuizQuestion(): LiveData<ArrayList<QuizQuestion>> = this.currQuizQuestion

    //Questions
    private val questions = MutableLiveData<ArrayList<QuizQuestion>>()

    fun getQuestions(): LiveData<ArrayList<QuizQuestion>> = this.questions

    //Question Number
    private val questionNumber = MutableLiveData<Int>()

    fun getQuestionNumber(): LiveData<Int> = this.questionNumber

    fun setQuestionNumber(questionNumber: Int) {
        this.questionNumber.value = questionNumber
    }

    fun incrementQuestionNumber() {
        this.questionNumber.value = this.questionNumber.value!! + 1
    }
    init {
        Log.d("diff init", "init")
    }
    //Difficulty
    private val difficulty = MutableLiveData<String>()

    fun getDifficulty(): LiveData<String> = this.difficulty

    fun setDifficulty(difficulty: String) {
        this.difficulty.value = difficulty
        Log.d("diffSet", this.difficulty.value.toString())
    }

    //Show Spinner
    private val showSpinner = MutableLiveData<Boolean>()

    fun getShowSpinner(): LiveData<Boolean> = showSpinner

    fun getRandomQuestion(context: Context) {
        this.showSpinner.value = true
        repository.fetchRandomQuestion(
            context,
            {
                for (q in it.results) {
                    Log.d("quiz", q.getDifficulty())
                }
                this.currQuizQuestion.value = it.results
                this.showSpinner.value = false
            },
            {
                this.currQuizQuestion.value = null
                this.showSpinner.value = false
            },
            difficulty.value!!
        )
    }
}