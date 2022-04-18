package com.softic.esraa.elnajjar

import android.app.Application
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Esraa Al-Najjar
 */
@HiltAndroidApp
open class ApplicationClass : Application()
{
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

    }
}