package com.tantnt.android.runstatistic.di.module

import android.app.Application
import androidx.room.Room
import com.tantnt.android.runstatistic.data.database.PracticeDao
import com.tantnt.android.runstatistic.data.database.PracticeDatabase
//import com.tantnt.android.runstatistic.data.database.getDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {
    /**
     * The method return the PracticeDatabase object
     */
    @Provides
    @Singleton
    internal fun provideDatase(application: Application): PracticeDatabase {
        return Room.databaseBuilder(
            application, PracticeDatabase::class.java, "practice")
            .allowMainThreadQueries().build()
    }

    /**
     * We need the PracticeDao module
     * For this, we need the PracticeDatabase object
     * So we will define the provider for this here in this module.
     */
    @Provides
    @Singleton
    internal fun providePracticeDao(practiceDatabase: PracticeDatabase): PracticeDao {
        return practiceDatabase.practiceDao
    }
}