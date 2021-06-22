package com.example.timekiller.network

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class DadJokeRequest : StringRequest{

    constructor(method: Int, url: String, listener: Response.Listener<String>,
    errorListener: Response.ErrorListener): super(method,url, listener, errorListener)

    constructor(url: String, listener: Response.Listener<String>,
                errorListener: Response.ErrorListener): super(url, listener, errorListener)

    override fun getHeaders(): MutableMap<String, String> {
        return mutableMapOf("Accept" to "text/plain")
    }
}