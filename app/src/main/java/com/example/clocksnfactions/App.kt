package com.example.clocksnfactions

import android.app.Application
import com.example.clocksnfactions.data.local.AppDatabase
import androidx.room.Room
import timber.log.Timber

class App : Application() {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "clocks_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
