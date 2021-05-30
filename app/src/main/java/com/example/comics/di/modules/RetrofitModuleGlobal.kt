package com.example.comics.di.modules


import com.example.comics.remote.ComicsInterface
import com.example.comics.utils.Constaints
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class RetrofitModuleGlobal {

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClientInstance(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient{

        return OkHttpClient.Builder()
            .connectTimeout(Constaints.CONNECTION_TIMEOUT, TimeUnit.SECONDS)       // establish connection to server
            .readTimeout(Constaints.READ_TIMEOUT, TimeUnit.SECONDS)                  // time between each byte read from the server //
            .writeTimeout(Constaints.WRITE_TIMEOUT, TimeUnit.SECONDS)               //time between each byte sent to server
            .retryOnConnectionFailure(false)
            .addInterceptor(loggingInterceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofitInstance( okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constaints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideComicsApi(retrofit: Retrofit): ComicsInterface{
        return retrofit.create(ComicsInterface::class.java)
    }

}