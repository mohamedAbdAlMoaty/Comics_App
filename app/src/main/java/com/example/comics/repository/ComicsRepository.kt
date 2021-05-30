package com.example.comics.repository

import com.example.comics.models.ComicsInfo
import com.example.comics.persistence.ComicsDao
import com.example.comics.remote.ComicsInterface
import javax.inject.Inject

class ComicsRepository @Inject constructor(private val comicsInterface: ComicsInterface, private val comicsDao: ComicsDao){
    suspend fun getComicsFromNetwork(id : Int) =  comicsInterface.getComics(id)

    suspend fun getComicsFromDb(comicsInfo: ComicsInfo?):List<ComicsInfo>{
        comicsDao.deleteAllRows()
        comicsDao.insertRow(comicsInfo)
        return  comicsDao.getAllRows()
    }

    suspend fun getComicsFromDb(): List<ComicsInfo> {
        return  comicsDao.getAllRows()
    }
}