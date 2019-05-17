package ru.observe.twits

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule{

    @Provides
    fun providesContext(application: AppWatchTwits): Context {
        return application.applicationContext
    }
}