package com.example.tripwise

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp

/**
 * Application which sets up our dependency [Graph] with a context.
 */

@HiltAndroidApp
class TripWiseApplication : Application(), Configuration.Provider {
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(if (BuildConfig.DEBUG) android.util.Log.DEBUG else android.util.Log.ERROR)
            .build()
}