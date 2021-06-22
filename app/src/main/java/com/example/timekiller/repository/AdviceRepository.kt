package com.example.timekiller.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.timekiller.db.daos.FavAdviceDao
import com.example.timekiller.db.entities.AdviceEntity
import com.example.timekiller.network.AdviceAPI
import com.google.gson.GsonBuilder

class AdviceRepository(private val dao : FavAdviceDao) {

    companion object {
        const val BASE_URL = "https://api.adviceslip.com/advice"
    }

    /**
     * Live data of favorite advices from database
     */
    val advices = dao.getAllFavAdvice()

    /**
     * Insert an adviceEntity to favorite_advice table in database
     * @param adviceEntity The adviceEntity marked as favorite to be inserted
     * @return the row id of the inserted adviceEntity
     */
    suspend fun insertFavAdvice(adviceEntity: AdviceEntity):Long{
        return dao.insertFavAdvice(adviceEntity)
    }

    /**
     * Look for an adviceEntity by its id
     * @param id The id of the adviceEntity to look up
     * @return the list of entries matched (1 if found, 0 if nonexistent)
     */
    suspend fun getFavAdviceById(id: Int) : List<AdviceEntity>{
        return dao.getFavAdviceById(id)
    }

    /**
     * Delete an adviceEntity from favorite_advice table in database
     * @param adviceEntity The adviceEntity unmarked as favorite to be delete
     * @return the row id of the deleted adviceEntity
     */
    suspend fun deleteFavAdvice(adviceEntity: AdviceEntity) : Int{
        return dao.deleteFavAdvice(adviceEntity)
    }

    /**
     * Delete sevaral adviceEntities given in a list
     * @param adviceEntities List of adviceEntities unmarked as favorite to be delete
     * @return the number of entries deleted?
     */
    suspend fun deleteFavAdvice(adviceEntities: List<AdviceEntity>) : Int{
        return dao.deleteFavAdvice(adviceEntities)
    }

    /**
     * Delete all entries in the favorite adviceEntity table
     */
    suspend fun deleteAllFavAdvice() : Int{
        return dao.deleteAll()
    }

    private val gson = GsonBuilder().create()

    /**
     * Fetches a random adviceEntity from the web service
     * @param context The application context
     * @param listener Callback function if a request succeeded
     * @param errorListener Callback function if a request failed
     */
    fun fetchRandomAdvice(
            context: Context,
            listener: Response.Listener<AdviceEntity>,
            errorListener: Response.ErrorListener
    ){
        val rq = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                BASE_URL,
                null,
                {
                    //convert JSONObject and invoke listener
                    val adviceHolder = gson.fromJson(it.toString(), AdviceAPI::class.java)
                    adviceHolder.slip.timestamp = System.currentTimeMillis()
                    listener.onResponse(adviceHolder.slip)
                },
                errorListener
        )
        jsonObjectRequest.setShouldCache(false)
        rq.add(jsonObjectRequest)
    }
}