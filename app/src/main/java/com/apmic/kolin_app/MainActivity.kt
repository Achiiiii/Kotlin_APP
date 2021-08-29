package com.apmic.kolin_app

import android.Manifest.permission
import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.jar.Manifest


class MainActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setCustomDensity(application, this)
        permissionsRequest()
    }

    private fun setCustomDensity(application: Application, activity: Activity) {
        val appDisplayMetrics = application.resources.displayMetrics

        // gain target density for dp: 360
        val targetDensity = appDisplayMetrics.widthPixels / 360.toFloat()
        val targetScaledDensity =
            targetDensity * (appDisplayMetrics.scaledDensity / appDisplayMetrics.density)
        val targetDensityDpi = (targetDensity * 160).toInt()

        //set application density
        appDisplayMetrics.density = targetDensity
        appDisplayMetrics.scaledDensity = targetScaledDensity
        appDisplayMetrics.densityDpi = targetDensityDpi

        //set activity density
        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaledDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
    }

    private fun permissionsRequest(){
        val arrayPermission = arrayOf(
            permission.INTERNET,
            permission.READ_EXTERNAL_STORAGE,
            permission.WRITE_EXTERNAL_STORAGE
        )

        requestPermissions(arrayPermission,1)
    }
}

