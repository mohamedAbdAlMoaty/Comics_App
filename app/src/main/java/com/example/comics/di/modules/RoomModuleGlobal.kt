package com.example.comics.di.modules
import android.content.Context
import androidx.room.Room
import com.example.comics.persistence.ComicsDao
import com.example.comics.persistence.ComicsDatabase
import com.example.comics.utils.Constaints.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class RoomModuleGlobal {

    @Singleton
    @Provides
    fun providePostDatabase(@ApplicationContext context: Context): ComicsDatabase {
        return Room
            .databaseBuilder(context, ComicsDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePostDAO(postDatabase: ComicsDatabase): ComicsDao {
        return postDatabase.postDao()
    }
}