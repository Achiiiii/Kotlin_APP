package com.apmic.kolin_app.app

import android.app.Application
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.apmic.kolin_app.util.BitmapCache

class AppController: Application() {
    private var mRequestQueue: RequestQueue? = null
    private var mImageLoader: ImageLoader? = null


    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }
    //create new request
    fun getRequestQueue(): RequestQueue? {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(applicationContext)
        }
        return mRequestQueue
    }
    //add bitmapcache in imageloader and get new request
    fun getImageLoader(): ImageLoader? {
        getRequestQueue()
        if (mImageLoader == null) {
            mImageLoader = ImageLoader(
                mRequestQueue,
                BitmapCache()
            )
        }
        return mImageLoader
    }
    //add to request
    fun <T> addToRequestQueue(req: Request<T>) {
        req.tag = TAG
        getRequestQueue()?.add(req)
    }

    companion object {
        val TAG = AppController::class.java.simpleName
        var mInstance: AppController? = null

        @Synchronized
        fun getInstance(): AppController? {

            return mInstance
        }
    }
}