package com.apmic.kolin_app.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.StatFs
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.Comparator

class ImageFileCacheUtils {
    fun getImage(url: String): Bitmap? {
        val path = directory + "/" + convertUrlToFileName(url)
        val file = File(path)
        return if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(path)
            if (bitmap == null) {
                file.delete()
                null
            } else {
                updateFileTime(path) //renew file update time
                bitmap
            }
        } else {
            null

        }
    }

    private val directory: String
        private get() = "$sDPath/$CACHEDIR"//get dir path

    //check SD card
    private val sDPath: String
        private get() {
            var sdDir: File? = null
            val sdCardExist: Boolean = Environment.getExternalStorageState()
                .endsWith(Environment.MEDIA_MOUNTED) //check SD card
            if (sdCardExist) {
                sdDir = Environment.getExternalStorageDirectory() //get dir path
            }
            return if (sdDir != null) {
                sdDir.toString()
            } else {
                ""
            }
        }

    private fun updateFileTime(path: String) {
        val file = File(path)
        val newModeifyTime = System.currentTimeMillis()
        file.setLastModified(newModeifyTime)
    }

    fun saveBitmapSmall(bitmap: Bitmap?, url: String) {
        if (bitmap == null) {
            return
        }
        //check space of SD card
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            return
        }
        val fileName = convertUrlToFileName(url)
        val dir = directory
        val dirFile = File(dir)
        if (!dirFile.exists()) {
            dirFile.mkdirs()
        }
        val file = File("$dir/$fileName")
        try {
            file.createNewFile()
            val outputStream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun saveBitmapHD(bitmap: Bitmap?, url: String) {
        if (bitmap == null) {
            return
        }
        //check space of SD card
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            return
        }
        val fileName = convertUrlToFileName(url)
        val dir = directory
        val dirFile = File(dir)
        if (!dirFile.exists()) {
            dirFile.mkdirs()
        }
        val file = File("$dir/$fileName")
        try {
            file.createNewFile()
            val outputStream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun freeSpaceOnSd(): Int {
        val statFs = StatFs(Environment.getExternalStorageDirectory().getPath())
        val sdFreeMB =
            statFs.availableBlocksLong * statFs.blockSizeLong / MB
        Log.i("SD card", "remain space：$sdFreeMB")
        return sdFreeMB.toInt()
    }

    private fun convertUrlToFileName(url: String): String {
        val strs = url.split("/").toTypedArray()
        return strs[strs.size - 1] + WHOLESALE_CONV
    }

    /**
     * add all file size under the dirpath
     * when the size of files exceed limit or SD card's space is less than FREE_SD_SPACE_NEEDED_TO_CACHE
     * then delete 40% files that have not been used recently
     */
    private fun removeCache(dirPath: String): Boolean {
        val dirFile = File(dirPath)
        val files: Array<File>? = dirFile.listFiles()
        if (files == null || files.size <= 0) {
            return true
        }
        //if doesn't have SD card
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return false
        }
        var dirSize = 0
        for (i in files.indices) {
            if (files[i].getName().contains(WHOLESALE_CONV)) {
                dirSize += files[i].length().toInt()
            }
        }
        if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            val removeFactor = (0.4 * files.size + 1).toInt()
            Arrays.sort(files, FileLastModifySoft())
            for (i in 0 until removeFactor) {
                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    files[i].delete()
                }
            }
        }
        return if (freeSpaceOnSd() <= CACHE_SIZE) {
            false
        } else true
    }

    private inner class FileLastModifySoft : Comparator<File?> {
        override fun compare(arg0: File?, arg1: File?): Int {
            return if (arg0?.lastModified()!! > arg1?.lastModified()!!) {
                1
            } else if (arg0?.lastModified() === arg1?.lastModified()) {
                0
            } else {
                -1
            }
        }
    }

    companion object {
        private const val CACHEDIR = "ImageCache" //cache dir
        private const val WHOLESALE_CONV = ".cache" //cache file
        private const val MB = 1024 * 1024
        private const val CACHE_SIZE = 80 //max cache size
        private const val FREE_SD_SPACE_NEEDED_TO_CACHE = 100 //min SD card space for cache

        var instance: ImageFileCacheUtils? = null
            get() {
                if (field == null) {
                    synchronized(ImageFileCacheUtils::class.java) {
                        if (field == null) {
                            field = ImageFileCacheUtils()
                        }
                    }
                }
                return field
            }
            private set

    }

    init {
        removeCache(directory)
    }
}