package com.example.timekiller.repository

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.timekiller.db.daos.FavComicDao
import com.example.timekiller.db.entities.ComicDataEntity
import com.google.gson.GsonBuilder
import kotlin.random.Random

class ComicRepository(private val dao: FavComicDao) {

    companion object {
        //use https://xkcd.com/info.0.json (current comic)
        //use https://xkcd.com/614/info.0.json (comic #614)

        private const val BASE_URL = "https://xkcd.com"
        private const val SUFFIX_URL = "/info.0.json"
    }

    private val gson = GsonBuilder().create()

    /**
     * Live data of comic datas from database
     */
    val comics = dao.getAllComic()

    /**
     * Insert a comic data to favorite table in database
     * @param comicDataEntity the data to be inserted
     * @return the row id of the inserted data
     */
    suspend fun insertFavComic(comicDataEntity: ComicDataEntity): Long {
        return dao.insertFavComic(comicDataEntity)
    }

    /**
     * Delete a comic data from favorite table in database
     * @param comicDataEntity the data to be delete
     * @return the row id of the deleted data
     */
    suspend fun deleteFavComic(comicDataEntity: ComicDataEntity): Int {
        return dao.deleteFavComic(comicDataEntity)
    }

    /**
     * Delete several comicDataEntities given in a list
     * @param comicDataEntities List of adviceEntities unmarked as favorite to be delete
     * @return the number of entries deleted?
     */
    suspend fun deleteFavComic(comicDataEntities: List<ComicDataEntity>) : Int{
        return dao.deleteFavComic(comicDataEntities)
    }

    /**
     * Delete all entries in the favorite comic table
     */
    suspend fun deleteAllFavComic(): Int {
        return dao.deleteAll()
    }

    /**
     * Gets the comic data of the newest comic available
     * @param rq Volley request queue
     * @param listener Callback function if the request succeeded
     * @param listener Callback function if the request failed
     */
    fun fetchCurrentComicData(
            rq: RequestQueue,
            listener: Response.Listener<ComicDataEntity>,
            errorListener: Response.ErrorListener
    ) {
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                BASE_URL + SUFFIX_URL,
                null,
                {
                    //convert JSONObject and invoke listener
                    listener.onResponse(gson.fromJson(it.toString(), ComicDataEntity::class.java))
                },
                errorListener
        )
        rq.add(jsonObjectRequest)
    }

    private fun fetchComicDataByNum(num : Int){

    }

    /**
     * Repeatedly fetches several the comic bitmaps by its **id**s from web service
     * @param nums List of comic codes to be fetched
     * @param context The application context
     * @param listener Callback function if a request succeeded
     * @param errorListener Callback function if a request failed
     */
    fun fetchComicImages(
            nums: Array<Int>,
            context: Context,
            listener: Response.Listener<Bitmap>,
            errorListener: Response.ErrorListener
    ) {
        val rq = Volley.newRequestQueue(context)
        for (i in nums) {
            Log.d("volley random", "i: " + i)
            Log.d("volley random", "url: $BASE_URL/$i$SUFFIX_URL")
            val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET,
                    "$BASE_URL/$i$SUFFIX_URL",
                    null,
                    {
                        Log.d("volley random", "json $it")
                        this.fetchComicImages(
                                arrayOf(gson.fromJson(it.toString(), ComicDataEntity::class.java).img),
                                context,
                                listener,
                                errorListener
                        )
                    },
                    errorListener
            )

            rq.add(jsonObjectRequest)
        }
    }

    /**
     * Repeatedly fetches several the comic bitmaps by its **url**s from web service
     * @param urls Array of urls to fetch the comics
     * @param context The application context
     * @param listener Callback function if a request succeeded
     * @param errorListener Callback function if a request failed
     */
    fun fetchComicImages(
            urls: Array<String>,
            context: Context,
            listener: Response.Listener<Bitmap>,
            errorListener: Response.ErrorListener
    ) {
        val rq = Volley.newRequestQueue(context)
        for (i in urls) {
            Log.d("volley random", "Img link: $i")
            val imageRequest = ImageRequest(
                    i,
                    listener,
                    0,
                    0,
                    ImageView.ScaleType.CENTER,
                    null,
                    errorListener
            )
            rq.add(imageRequest)
        }
    }

    /**
     * Fetches a bitmap and a data for a random comic from the web
     * @param context The application context
     * @param listener Callback function if the request succeeded
     * @param errorListener Callback function if the request failed
     */
    fun fetchRandomComic(
            context: Context,
            listener: Response.Listener<Bitmap>,
            errorListener: Response.ErrorListener
    ) {
        this.fetchCurrentComicData(
                Volley.newRequestQueue(context),
                {
                    val num = Random.nextInt(it.num) + 1
                    Log.d("volley random", "chosen num: $num")
                    this.fetchComicImages(arrayOf(num), context, listener, errorListener)
                },
                errorListener
        )
    }

    /**
     * Calling method getFavComicById from dao
     * @param id comic id
     */
    suspend fun getFavComicById(id: Int): List<ComicDataEntity> = dao.getFavComicById(id)

    /**
     * Fetches several the comic data by its **id**s from web service
     * @param nums List of comic codes to be fetched
     * @param context The application context
     * @param listener Callback function if a request succeeded
     * @param errorListener Callback function if a request failed
     */
    fun fetchComicData(
        num : Int,
        context: Context,
        listener: Response.Listener<ComicDataEntity>,
        errorListener: Response.ErrorListener
    ) {
        val rq = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            "$BASE_URL/$num$SUFFIX_URL",
            null,
            {
                listener.onResponse(gson.fromJson(it.toString(), ComicDataEntity::class.java))
            },
            errorListener
        )

        rq.add(jsonObjectRequest)
    }
}