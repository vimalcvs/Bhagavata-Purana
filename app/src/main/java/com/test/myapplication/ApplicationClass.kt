package com.test.myapplication

import android.app.Application
import com.google.android.material.color.DynamicColors

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

    }
}