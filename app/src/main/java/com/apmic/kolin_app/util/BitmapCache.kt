package com.apmic.kolin_app.util

import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader

class BitmapCache() : ImageLoader.ImageCache {
    var mCache: LruCache<String, Bitmap>? = null
    val sdCard: Boolean = android.os.Environment.getExternalStorageState().endsWith(android.os.Environment.MEDIA_MOUNTED)

    fun BitmapCache(){
        val maxSize = (Runtime.getRuntime().maxMemory() / 8).toInt()//10M
        mCache = object : LruCache<String, Bitmap>(maxSize){
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                if (value == null){
                    return 0
                }
                else
                    return value.rowBytes * value.height
            }
        }

    }

    override fun getBitmap(url: String): Bitmap? {
        return ImageFileCacheUtils.instance?.getImage(url)
    }


    override fun putBitmap(url: String, bitmap: Bitmap) {
            ImageFileCacheUtils.instance?.saveBitmapSmall(bitmap, url)
    }

}