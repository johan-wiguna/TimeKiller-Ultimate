package com.example.timekiller.repository

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.timekiller.model.Joke
import com.example.timekiller.model.JokeThree
import com.example.timekiller.model.JokeTwo
import com.example.timekiller.network.DadJokeRequest
import com.example.timekiller.network.JokeThreeAPI
import com.google.gson.GsonBuilder

class JokeRepository {

    companion object {
        const val BASE_URL = "https://official-joke-api.appspot.com/random_joke"
        const val BASE_URL_2 = "https://icanhazdadjoke.com"
        const val BASE_URL_3 = "http://api.icndb.com/jokes/random"
    }

    private val gson = GsonBuilder().create()

    /**
     * Fetches a random joke from the web service
     * @param context The application context
     * @param listener Callback function if a request succeeded
     * @param errorListener Callback function if a request failed
     */
    fun fetchRandomJoke(
        context: Context,
        listener: Response.Listener<Joke>,
        errorListener: Response.ErrorListener
    ){
        val rq = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            BASE_URL,
            null,
            {
                //convert JSONObject and invoke listener
                listener.onResponse(gson.fromJson(it.toString(), Joke::class.java))
            },
            errorListener
        )
        jsonObjectRequest.setShouldCache(false)
        rq.add(jsonObjectRequest)
    }

    /**
     * Fetches a random joke from the second web service
     * @param context The application context
     * @param listener Callback function if a request succeeded
     * @param errorListener Callback function if a request failed
     */
    fun fetchRandomJokeTwo(
        context: Context,
        listener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ){
        val rq = Volley.newRequestQueue(context)
        val stringRequest = DadJokeRequest(
            Request.Method.GET,
            BASE_URL_2,
            {
                //convert JSONObject and invoke listener
                listener.onResponse(it)
                Log.d("dad joke req",it)
            },
            errorListener
        )
        stringRequest.setShouldCache(false)
        rq.add(stringRequest)
    }

    /**
     * Fetches a random joke from the third web service
     * @param context The application context
     * @param listener Callback function if a request succeeded
     * @param errorListener Callback function if a request failed
     */
    fun fetchRandomJokeThree(
        context: Context,
        listener: Response.Listener<JokeThreeAPI>,
        errorListener: Response.ErrorListener
    ){
        val rq = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            BASE_URL_3,
            null,
            {
                //convert JSONObject and invoke listener
                listener.onResponse(gson.fromJson(it.toString(), JokeThreeAPI::class.java))
            },
            errorListener
        )
        jsonObjectRequest.setShouldCache(false)
        rq.add(jsonObjectRequest)
    }

}