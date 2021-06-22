package com.example.timekiller.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timekiller.model.Joke
import com.example.timekiller.model.JokeThree
import com.example.timekiller.model.JokeTwo
import com.example.timekiller.repository.JokeRepository

class JokeViewModel: ViewModel(){
    //Angka random untuk memilih API
    private var randomApi: Int = 0

    //Jokes
    private val repository = JokeRepository()

    private val currJoke = MutableLiveData<Joke?>()
    fun getCurrJokes(): LiveData<Joke?> = currJoke

    private val currJokeTwo = MutableLiveData<String>()
    fun getCurrJokesTwo(): LiveData<String> = currJokeTwo

    private val currJokeThree = MutableLiveData<JokeThree?>()
    fun getCurrJokesThree(): LiveData<JokeThree?> = currJokeThree

    /**
     * Live data of joke data fetched from web service
     * The repository will store the retrieved joke data here on a successful request
     */
    private val errorMessage = MutableLiveData<String>()
    fun getErrorMessage(): LiveData<String> = errorMessage

    /**
     * Live Data of a flag signifying whether the spinner should show or not
     * Spinner should show when app is waiting for network response and should hide otherwise
     */
    private val showSpinner = MutableLiveData<Boolean>()
    fun getShowSpinner(): LiveData<Boolean> = showSpinner

    /**
     * Fetch a random joke, will be stored in currJoke live data
     * @param context The application context
     */
    fun getRandomJoke(context: Context){
        this.showSpinner.value = true
        Log.d("volley joke", "fetching")
        repository.fetchRandomJoke(
            context,
            {
                Log.d("volley joke", "done")
                this.currJoke.value = it
                Log.d("hasil fetch", it.getSetup())
                this.showSpinner.value = false
            },
            {
                Log.d("volley error", it.message.toString())
                this.currJoke.value = null
                this.showSpinner.value = false
            }
        )
    }

    /**
     * Fetch a random joke, will be stored in currJoke live data
     * @param context The application context
     */
    fun getRandomJokeTwo(context: Context){
        this.showSpinner.value = true
        Log.d("volley joke 2", "fetching")
        repository.fetchRandomJokeTwo(
            context,
            {
                Log.d("volley joke 2", "done")
                this.currJokeTwo.value = it
                Log.d("hasil fetch 2", it)
                this.showSpinner.value = false
            },
            {
                Log.d("volley error", it.message.toString())
                this.currJokeTwo.value = null
                this.showSpinner.value = false
            }
        )
    }

    /**
     * Fetch a random joke, will be stored in currJoke live data
     * @param context The application context
     */
    fun getRandomJokeThree(context: Context){
        this.showSpinner.value = true
        Log.d("volley joke 3", "fetching")
        repository.fetchRandomJokeThree(
            context,
            {
                Log.d("volley joke 3", "done")
                this.currJokeThree.value = it.value
                Log.d("hasil fetch 3", it.value.getJoke())
                this.showSpinner.value = false
            },
            {
                Log.d("volley error 3", it.message.toString())
                this.currJokeThree.value = null
                this.showSpinner.value = false
            }
        )
    }

    /**
     * Fetch a random joke from different APIs
     * @param context The application context
     */
    fun getRandomizedJoke(context: Context){
        randomApi = (0 until 4).random()
        if(randomApi == 1){
            getRandomJoke(context)
        }else if(randomApi == 2){
            getRandomJokeTwo(context)
        }else if(randomApi == 3){
            getRandomJokeThree(context)
        }
    }
}
