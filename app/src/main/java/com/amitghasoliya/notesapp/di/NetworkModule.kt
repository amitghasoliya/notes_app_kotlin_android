package com.amitghasoliya.notesapp.di

import com.amitghasoliya.notesapp.api.AuthInterceptor
import com.amitghasoliya.notesapp.api.NoteAPI
import com.amitghasoliya.notesapp.api.UserAPI
import com.amitghasoliya.notesapp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient{
        val client = OkHttpClient.Builder().addInterceptor(authInterceptor)
        client.connectTimeout(45,TimeUnit.SECONDS)
        client.readTimeout(45, TimeUnit.SECONDS)
        client.writeTimeout(45, TimeUnit.SECONDS)
        return client.build()
    }

    @Singleton
    @Provides
    fun providesNoteAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): NoteAPI{
        return retrofitBuilder
            .client(okHttpClient)
            .build().create(NoteAPI::class.java)
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): UserAPI{
        val client = OkHttpClient.Builder()
        client.connectTimeout(45, TimeUnit.SECONDS)
        client.readTimeout(45, TimeUnit.SECONDS)
        client.writeTimeout(45, TimeUnit.SECONDS)
        val okHttpClient = client.build()
        return retrofitBuilder.client(okHttpClient).build().create(UserAPI::class.java)
    }
}