package com.android.rakuten

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RakutenApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        var application: Application? = null
        fun getContext(): Context? {
            return application?.applicationContext
        }
    }
}