package com.example.flickr.di.qualifier

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class Month(
    /** The name.  */
    val value: String = ""
)