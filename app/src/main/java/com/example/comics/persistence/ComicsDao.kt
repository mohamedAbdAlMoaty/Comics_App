package com.example.comics.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.comics.models.ComicsInfo
import com.example.comics.utils.Constaints.Companion.DATABASE_NAME

@Dao
interface ComicsDao {
    @Query("SELECT * FROM " + DATABASE_NAME)
    suspend fun getAllRows(): List<ComicsInfo>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRow (comicsInfo: ComicsInfo?)

    @Query("DELETE FROM "+ DATABASE_NAME)
    suspend fun deleteAllRows()
}