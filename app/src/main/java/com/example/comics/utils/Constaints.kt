package com.example.comics.utils

class Constaints {

    companion object {
        const val BASE_URL:String = "http://xkcd.com/"
        const val DATABASE_NAME="comics"
        const val CONNECTION_TIMEOUT:Long = 10 // 10 seconds
        const val READ_TIMEOUT :Long = 2 // 2 seconds
        const val WRITE_TIMEOUT :Long = 2 // 2 seconds
        const val CHANNEL_ID:String ="channel_id"
    }

}