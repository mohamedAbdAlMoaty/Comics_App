package com.example.comics.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.comics.models.ComicsInfo
import com.example.comics.persistence.ComicsDao

@Database(entities = [ComicsInfo::class ], version = 5, exportSchema = false)
abstract class ComicsDatabase : RoomDatabase() {

    abstract fun postDao(): ComicsDao
}