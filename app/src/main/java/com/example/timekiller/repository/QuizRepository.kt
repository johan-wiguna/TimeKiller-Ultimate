package com.example.timekiller.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.timekiller.network.QuizAPI
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray

class QuizRepository {

    companion object {
        const val BASE_URL = "https://opentdb.com/api.php?amount=20&difficulty="
    }

    private val gson = GsonBuilder().create()

    /**
     * Fetches a random question from the web service
     * @param context The application context
     * @param listener Callback function if a request succeeded
     * @param errorListener Callback function if a request failed
     */
    fun fetchRandomQuestion(
            context: Context,
            listener: Response.Listener<QuizAPI>,
            errorListener: Response.ErrorListener,
            difficulty: String
    ){
        Log.d("URL", BASE_URL + difficulty + "&encode=url3986")
        val rq = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + difficulty + "&encode=url3986",
                null,
                {
                    //convert JSONObject and invoke listener
                    listener.onResponse(gson.fromJson(it.toString(), QuizAPI::class.java))
                },
                errorListener
        )
        jsonObjectRequest.setShouldCache(false)
        rq.add(jsonObjectRequest)
    }
}