package com.bradleytenuta.stoutandabout

import android.app.Application
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class StoutAndAboutApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize the global pub store on startup
        MainScope().launch {
            PubDataStore.initialize(this@StoutAndAboutApplication)
        }
    }
}
