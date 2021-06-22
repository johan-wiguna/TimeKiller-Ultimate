package com.example.timekiller.util

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtil {
    companion object {
        /**
         * Checks if the device is connected to internet
         */
        fun isNetworkConnected(context: Context): Boolean{
            val connMgr: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connMgr.activeNetworkInfo != null
        }
    }
}