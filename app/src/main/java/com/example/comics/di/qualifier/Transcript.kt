package com.example.carlistsample.di.qualifier

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class Transcript(
    /** The name.  */
    val value: String = ""
)