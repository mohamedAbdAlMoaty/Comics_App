package com.example.carlistsample.di.qualifier

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class Alt(
    /** The name.  */
    val value: String = ""
)