package com.jetpack.demo

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import main.MainApplication

class TestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, MainApplication::class.java.name, context)
    }
}