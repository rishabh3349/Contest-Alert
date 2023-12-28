package com.example.contestalert

import android.app.Application
import com.example.contestalert.data.AppContainer
import com.example.contestalert.data.DefaultAppContainer

class ContestAlertApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}