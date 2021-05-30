package com.example.comics.di.qualifier

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class Title(
    /** The name.  */
    val value: String = ""
)