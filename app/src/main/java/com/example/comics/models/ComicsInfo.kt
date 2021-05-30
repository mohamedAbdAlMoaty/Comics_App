package com.example.comics.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.carlistsample.di.qualifier.Alt
import com.example.carlistsample.di.qualifier.Img
import com.example.carlistsample.di.qualifier.Num
import com.example.carlistsample.di.qualifier.Transcript
import com.example.comics.di.qualifier.Title
import com.example.comics.utils.Constaints.Companion.DATABASE_NAME
import com.example.flickr.di.qualifier.Day
import com.example.flickr.di.qualifier.Month
import com.example.flickr.di.qualifier.Year
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.scopes.ActivityScoped
import java.io.Serializable

@ActivityScoped
@Entity(tableName = DATABASE_NAME)
class ComicsInfo (
    @PrimaryKey()
    @ColumnInfo(name = "num")
    @SerializedName("num")
    @Num
    var num: Int,

    @ColumnInfo(name = "day")
    @SerializedName("day")
    @Day
    var day: String,
    @ColumnInfo(name = "month")
    @SerializedName("month")
    @Month
    var month: String,
    @ColumnInfo(name = "year")
    @SerializedName("year")
    @Year
    var year: String,


    @ColumnInfo(name = "img")
    @SerializedName("img")
    @Img
    var img: String,

    @ColumnInfo(name = "transcript")
    @SerializedName("transcript")
    @Transcript
    var transcript: String,

    @ColumnInfo(name = "alt")
    @SerializedName("alt")
    @Alt
    var alt: String,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Title
    var title: String
    ) :  Serializable{}